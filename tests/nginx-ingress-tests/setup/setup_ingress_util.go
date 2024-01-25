package setup

import (
	"errors"
	"os"
	"strings"
	"testing"

	"github.com/Jeffail/gabs"
	"github.com/gruntwork-io/terratest/modules/files"
	"github.com/gruntwork-io/terratest/modules/terraform"
)

// This helper method takes in a blueprint.json's path and does a terraform init
// & apply. It also returns you the `terraformOptions` used if you want to run
// terraform commands in your code again
func CreateFromBlueprint(t *testing.T, blueprintPath string) *terraform.Options {
	terraformOptions := getTerraformOptions(t, blueprintPath)

	terraform.InitAndApply(t, terraformOptions)
	return terraformOptions
}

// This method does the opposite of `CreateFromBlueprint` and deletes all the
// ingress resources created. It also deletes resources marked with
// `prevent_destroy`. The destroy part is called in later Taskfile steps so your
// test code may not need to explicitly call this.
func DeleteFromBlueprint(t *testing.T, blueprintPath string) {
	terraformOptions := getTerraformOptions(t, blueprintPath)

	err := alterPreventDestroy(false)
	if err != nil {
		t.Fatal(err)
	}
	defer alterPreventDestroy(true)

	// in case the setup part is *never* run, destroy shouldnt fail because
	// terraform was never initialised
	terraform.Init(t, terraformOptions)
	terraform.Destroy(t, terraformOptions)
	os.Remove("../nginx_ingress_controller/providers.tf")
}

// Destroy's only the ingress controller. Useful when terraform apply will
// create a new loadbalancer such as in the case of `private=true`. This will
// help avoid the cases when dns records are not updating to the new LB's url
func DestroyIngressController(t *testing.T, terraformOptions *terraform.Options) {
	var tfOptionsCopy terraform.Options = *terraformOptions
	tfOptionsCopy.Targets = []string{"helm_release.nginx_ingress_ctlr"}

	err := alterPreventDestroy(false)
	if err != nil {
		t.Fatal(err)
	}
	defer alterPreventDestroy(true)

	terraform.Destroy(t, &tfOptionsCopy)
}

func getTerraformOptions(t *testing.T, blueprintPath string) *terraform.Options {
	blueprint, err := os.ReadFile(blueprintPath)
	if err != nil {
		t.Fatal(err)
	}

	// copy provider config file
	err = files.CopyFile("../setup/providers.tf", "../nginx_ingress_controller/providers.tf")
	if err != nil {
		t.Fatal(err)
	}

	blueprintParsed, err := gabs.ParseJSON(blueprint)
	if err != nil {
		t.Fatal(err)
	}

	baseDomain := GetFromEnv(t, "INGRESS_TESTING_HOSTED_ZONE")
	baseDomainId := GetFromEnv(t, "INGRESS_TESTING_HOSTED_ZONE_ID")

	return terraform.WithDefaultRetryableErrors(t, &terraform.Options{
		TerraformDir: "../nginx_ingress_controller",
		Vars: map[string]interface{}{
			// "count":         count,
			"instance":      blueprintParsed.String(),
			"instance_name": "test-ingress", // when changing this, update userId in basicauth test too
			"environment": map[string]interface{}{
				"namespace":   NAMESPACE,
				"cloud":       "AWS", // TODO: AWS is hardcoded for now
				"unique_name": "unq",
				"timezone":    "Asia/Kolkata",

				"registry_secret_objects": []map[string]interface{}{
					{"name": "registry-secret"},
				},
			},
			// "cloud_tags": map[string]interface{}{
			// 	"facetscontrolplane": "facetsdemo",
			// 	"cluster": "azure-infra-ci",
			// 	"facetsclustername": "azure-infra-ci",
			// },
			"cc_metadata": map[string]interface{}{
				"tenant_base_domain":    baseDomain,
				"tenant_base_domain_id": baseDomainId,
			},
		},
	})
}

// changes `prevent_destroy` value to the boolean value passed as argument
func alterPreventDestroy(preventDestoryVal bool) error {
	const fileToReplace = "../nginx_ingress_controller/main.tf"
	file, err := os.ReadFile(fileToReplace)
	if err != nil {
		return err
	}
	var old, _new string
	if preventDestoryVal { // if prevent des
		old = "prevent_destroy = false"
		_new = "prevent_destroy = true"
	} else {
		old = "prevent_destroy = true"
		_new = "prevent_destroy = false"
	}
	newContent := strings.ReplaceAll(string(file), old, _new)

	err = os.WriteFile(fileToReplace, []byte(newContent), 0664)
	return err
}

// Parses the terraform output and returns a gabs container so you can query the
// json output. You can inspect the output values by calling this method and
// seeing the test logs which will have this json printed out
func ParseOutput(t *testing.T, terraformOptions *terraform.Options) *gabs.Container {
	parsed, err := gabs.ParseJSON([]byte(terraform.OutputJson(t, terraformOptions, "")))
	if err != nil {
		t.Fatal("unable to parse json", err)
	}
	return parsed
}

// parses the json from the echoserver image and returns the hostname
func ParseEchoHostname(body string) (hostname string, err error) {
	jsonParsed, err := gabs.ParseJSON([]byte(body))
	if err != nil {
		return "", err
	}
	hostname, ok := jsonParsed.Path("os.hostname").Data().(string)
	if !ok {
		return "", errors.New("os.hostname does not exist")
	}
	return hostname, nil
}

// Factory method for generating custom terratest validation methods which
// validates that the echoserver response is generated from the expected echoserver
func MakeHostnameValidator(test PathHostnameLink) func(int, string) bool {
	if test.ExpectedStatusCode == 0 {
		test.ExpectedStatusCode = 200
	}
	return func(code int, body string) bool {
		if code != test.ExpectedStatusCode {
			return false
		}
		if test.SkipBody {
			return true
		}
		hostname, err := ParseEchoHostname(body)
		if err != nil {
			panic(err)
		}
		if hostname != test.ExpectedHostname {
			return false
		}
		return true
	}
}
