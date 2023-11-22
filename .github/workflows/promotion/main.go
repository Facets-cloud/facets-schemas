package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"slices"
	"strings"
	"sync"
	"flag"
	"os"
)

type APIConfig struct {
	URL          string `json:"URL"`
	Username     string `json:"Username"`
	Password     string `json:"Password"`
	QueryParams  map[string]string
	Name         string `json:"Name"`
	ClusterID    string
	AbsolutePath string
	IncludeCluster []string `json:"IncludeCluster"`
}

type ApiResponse struct {
	Config  APIConfig
	Content []struct {
		TFVersion   string `json:"tfVersion"`
		ReleaseType string `json:"releaseType"`
		ID          string `json:id`
		Name        string `json:name`
		StackName   string `json:stackName`
		Status      string `json:status`
	} `json:"content"`
}

func makeAPICall(config APIConfig, resultChannel chan<- *ApiResponse, wg *sync.WaitGroup, path string) {
	defer wg.Done()
	apiEndpoint := config.URL + path
	// Create a URL with query parameters
	u, err := url.Parse(apiEndpoint)
	if err != nil {
		fmt.Printf("Error parsing URL: %v\n", err)
		resultChannel <- nil
		return
	}

	q := u.Query()
	for key, value := range config.QueryParams {
		q.Add(key, value)
	}
	u.RawQuery = q.Encode()

	// Create an HTTP request with basic authentication
	req, err := http.NewRequest("GET", u.String(), nil)
	if err != nil {
		fmt.Printf("Error creating request: %v\n", err)
		resultChannel <- nil
		return
	}
	req.SetBasicAuth(config.Username, config.Password)

	// Make the HTTP request
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Printf("Error making request: %v\n", err)
		resultChannel <- nil
		return
	}
	defer resp.Body.Close()

	// Process the response
	if resp.StatusCode == http.StatusOK {

		// Read and parse the JSON response body into the ApiResponse struct
		var response ApiResponse
		response.Config = config
		if err := json.NewDecoder(resp.Body).Decode(&response); err != nil {
			fmt.Printf("Error parsing JSON response: %v\n", err)
			resultChannel <- nil
			return
		}

		resultChannel <- &response
	} else {
		fmt.Printf("API call to %s%s failed with status code: %d\n", config.URL, path, resp.StatusCode)
		resultChannel <- nil
	}
}

func main() {

	var tag string
	flag.StringVar(&tag, "tag", "", "Tag to promote")
	flag.Parse()
	var control_planes map[string]APIConfig
	var passwordMap map[string]string
    file, err := os.Open("control_planes.json")
    if err != nil {
    	fmt.Println("Error opening JSON file:", err)
    	return
    }
    decoder := json.NewDecoder(file)
    if err := decoder.Decode(&control_planes); err != nil {
    	fmt.Println("Error decoding JSON:", err)
    	return
    }
    defer file.Close()
    file, err = os.Open("passwords.json")
    if err != nil {
        fmt.Println("Error opening JSON file:", err)
        return
    }
    decoder = json.NewDecoder(file)
    if err := decoder.Decode(&passwordMap); err != nil {
        fmt.Println("Error decoding JSON:", err)
        return
    }
    defer file.Close()
    for key, token := range passwordMap {
    	if apiConfig, exists := control_planes[key]; exists {
    		apiConfig.Password = token
    		control_planes[key] = apiConfig
    	}
    }
	var wg sync.WaitGroup
	controlPlaneChannel := make(chan *ApiResponse, len(control_planes))
	for _, control_plane := range control_planes {
		wg.Add(1)
		control_plane.QueryParams = map[string]string{
			"sortBy": "ID",
		}
		go makeAPICall(control_plane, controlPlaneChannel, &wg, "/cc-ui/v1/stacks/clusters")
	}
	wg.Wait()
	close(controlPlaneChannel)
	clusters := make(map[string]map[string]interface{})
	// Process the results
	for result := range controlPlaneChannel {
		if result != nil {
			for _, content := range result.Content {
				absolutePath := result.Config.Name + " -> " + content.StackName + " -> " + content.Name
				if slices.Contains(result.Config.IncludeCluster,content.Name) {
				    clusters[absolutePath] = map[string]interface{}{
                    	"clusterID":    content.ID,
                    	"URL":          result.Config.URL,
                    	"Username":     result.Config.Username,
                    	"Password":     result.Config.Password,
                    	"AbsolutePath": absolutePath,
                    }
				}
			}
		} else {
			panic("At least one API call failed to fetch list of the clusters. Exiting...")
            break
		}
	}
	resultChannel := make(chan *ApiResponse, len(clusters))
	for _, cluster := range clusters {
		config := APIConfig{
			URL:      cluster["URL"].(string),
			Username: cluster["Username"].(string),
			Password: cluster["Password"].(string),
			QueryParams: map[string]string{
			    "excludeStatus": "FAULT",
			    "pageSize": "100",
			},
			ClusterID:    cluster["clusterID"].(string),
			AbsolutePath: cluster["AbsolutePath"].(string),
		}
		apiPath := "/cc-ui/v1/clusters/" + cluster["clusterID"].(string) + "/deployments/search"
		wg.Add(1)
		go makeAPICall(config, resultChannel, &wg, apiPath)
	}
	wg.Wait()
	close(resultChannel)
	failedReleases := make(map[string]map[string]string)
	noReleases := make([]string,0)
	excludeStatus := []string{"SUCCEEDED", "FAULT", "IN_PROGRESS"}
	// Process the results
	for result := range resultChannel {
	    absolutePath := result.Config.AbsolutePath
		if result != nil {
			releasesFromTag := make([]map[string]string, 0)
			for _, content := range result.Content {
				if strings.Contains(content.TFVersion, "stage/"+tag) {
					releaseLink := result.Config.URL + "/capc/capillary-cloud/cluster/" + result.Config.ClusterID + "/release-details/" + content.ID
					releasesFromTag = append(releasesFromTag, map[string]string{
						"absolutePath": absolutePath,
						"releaseLink":  releaseLink,
						"status":       content.Status,
						"ReleaseType": content.ReleaseType,
					})
				}
			}
			if len(releasesFromTag) == 0 {
                noReleases = append(noReleases,absolutePath)
			}
			for _, release := range releasesFromTag {
			    if (release["ReleaseType"] == "RELEASE") {
			        if (!slices.Contains(excludeStatus, release["status"])) {
			            failedReleases[release["absolutePath"]] = map[string]string{
                            "releaseLink": release["releaseLink"],
                            "status":      release["status"],
                        }
			        }
                    break
			    }
			}
			if len(releasesFromTag) > 0 {
				if !slices.Contains([]string{"SUCCEEDED", "FAULT", "IN_PROGRESS"}, releasesFromTag[0]["status"]) {
					failedReleases[releasesFromTag[0]["absolutePath"]] = map[string]string{
						"releaseLink": releasesFromTag[0]["releaseLink"],
						"status":      releasesFromTag[0]["status"],
					}
				}
			}
		} else {
			panic("At least one API call failed to fetch release details. Exiting...")
			break
		}
	}
	if len(noReleases) > 0 {
	    fmt.Printf("Tag %s could not be promoted to production. No releases found for the following clusters. \n", tag)
	    for _, absolutePath := range noReleases {
	        fmt.Printf("%s\n",absolutePath)
	    }
	    os.Exit(1)
	} else {
	    if len(failedReleases) > 0 {
        	fmt.Printf("Tag %s could not be promoted to production due to the following release failures.\n", tag)
        	for absolutePath, release := range failedReleases {
        		fmt.Printf(" <%s|%s>: %s \n", release["releaseLink"],absolutePath, release["status"])
        	}
        	os.Exit(1)
        } else {
        	fmt.Printf("Tag %s successfully promoted to production.\n", tag)
       }
	}
}
