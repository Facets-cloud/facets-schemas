package setup

import (
	"bytes"
	"fmt"
	"os"
	"strconv"
	"sync"
	"testing"
	"text/template"
	"time"

	"github.com/gruntwork-io/terratest/modules/k8s"
	"github.com/gruntwork-io/terratest/modules/logger"
)

type EchoServer struct {
	Count     int
	Name      string
	Namespace string
}

func NewDefaultEchoServer() EchoServer {
	var count int
	countStr, err := os.LookupEnv("ECHO_SERVER_COUNT")
	if err {
		count = 3
	}
	count, err_ := strconv.Atoi(countStr)
	if err_ != nil {
		panic("ECHO_SERVER_COUNT is not a valid integer")
	}
	echoServer := EchoServer{
		Count:     count,
		Name:      "echoserver-",
		Namespace: NAMESPACE,
	}

	return echoServer
}

func genTemplate(count int, echoServerConfig EchoServer) string {
	buffer := new(bytes.Buffer)
	templ, err := os.ReadFile("http-echo.yaml.gotmpl")
	if err != nil {
		panic(err)
	}
	echoServerConfig.Count = count
	_templ := template.Must(template.New("manifest").Parse(string(templ)))
	err = _templ.Execute(buffer, echoServerConfig)
	if err != nil {
		panic(err)
	}

	return buffer.String()
}

func _echoServerCreationDeletion(t *testing.T, delete bool) {
	var wg sync.WaitGroup
	echoServerConfig := NewDefaultEchoServer()

	var kubeErr error
	for i := 1; i <= echoServerConfig.Count; i++ {
		manifest := genTemplate(i, echoServerConfig)

		wg.Add(1)
		go func(wg *sync.WaitGroup, i int) {
			defer wg.Done()
			if delete {
				kubeErr = k8s.KubectlDeleteFromStringE(t, &k8s.KubectlOptions{}, manifest)
				if kubeErr != nil {
					// just print what it was, generally it is not found error
					logger.Log(t, "Error while deleting:", kubeErr, "\n")
					kubeErr = nil
				}
			} else {
				kubeErr = k8s.KubectlApplyFromStringE(t, &k8s.KubectlOptions{}, manifest)
				if kubeErr != nil { // let the main test case handle the error early
					return
				}
				time.Sleep(10 * time.Second)
				k8s.WaitUntilServiceAvailable(
					t,
					&k8s.KubectlOptions{Namespace: echoServerConfig.Namespace},
					fmt.Sprintf("%s%d", echoServerConfig.Name, i),
					10,
					10*time.Second,
				)
				k8s.WaitUntilPodAvailable(
					t,
					&k8s.KubectlOptions{Namespace: echoServerConfig.Namespace},
					fmt.Sprintf("%s%d", echoServerConfig.Name, i),
					20,
					10*time.Second,
				)
			}
		}(&wg, i)
	}
	wg.Wait()
	if kubeErr != nil {
		verb := "create"
		if delete {
			verb = "delete"
		}
		t.Fatal(
			fmt.Sprintf("Could not %s echo server deployments", verb),
			kubeErr,
		)
	}
}
func TestSetupEchoServers(t *testing.T) {
	// this is deleted when ingress is deleted because deleting namespace with
	// echoservers also deletes the ingress setup.
	// intentionally ignore error when creating namespace
	k8s.CreateNamespaceE(t, &k8s.KubectlOptions{}, NAMESPACE)
	_echoServerCreationDeletion(t, false)
}

func TestDestroyEchoServers(t *testing.T) {
	_echoServerCreationDeletion(t, true)
}
