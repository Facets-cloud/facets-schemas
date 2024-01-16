package misctest

import (
	"crypto/tls"
	"encoding/base64"
	"testing"
	"time"

	"github.com/Facets-cloud/facets-iac/tests/nginx-ingress-tests/setup"
	http_helper "github.com/gruntwork-io/terratest/modules/http-helper"
	"github.com/gruntwork-io/terratest/modules/retry"
)

const BLUEPRINT = "./basic_auth_intent.json"

func TestBasicAuth(t *testing.T) {
	terraformOptions := setup.CreateFromBlueprint(t, BLUEPRINT)
	terraformOutput := setup.ParseOutput(t, terraformOptions)

	domainName, err := setup.GetDomainName(terraformOutput, "1")
	if err != nil {
		t.Fatal(err)
	}

	setup.WaitUntilDnsExists(t, domainName)

	authSecret, err := setup.GetDomainName(terraformOutput, "0") // hacky method, since auth credential is at 0th index in output
	if err != nil {
		t.Fatal("unable to parse auth secret", err)
	}

	// validate 401 auth required
	http_helper.HttpGetWithRetryWithCustomValidation(
		t,
		"http://"+domainName,
		&tls.Config{},
		10, 10*time.Second,
		func(i int, s string) bool {
			return i == 401
		},
	)

	// construct the base64 basic auth payload
	// Example Base64 conversion:
	// 	b64:	GVzdC1pbmdyZXNzLWNvbnRyb2xsZXJ1c2VyOiQyYSQxMCREN05NV25ST1drRm9NYWxrajVuVTN1YWhIZnVMbVpodmF3QnFEZXlUQmUuMjltOHRKTkw1Sw==
	//  out:    test-ingress-controlleruser:$2a$10$D7NMWnROWkFoMalkj5nU3uahHfuLmZhvawBqDeyTBe.29m8tJNL5K
	const userId = "test-ingress" + "user" // instance_name +
	payload := base64.StdEncoding.EncodeToString([]byte(userId + ":" + authSecret))

	// now validate that after passing in auth credentials we have 200
	retry.DoWithRetry(
		t,
		"HTTP Do with Auth header",
		10, 10*time.Second,
		func() (string, error) {
			status, output, err := http_helper.HTTPDoWithOptionsE(
				t,
				http_helper.HttpDoOptions{
					Method: "GET",
					Url:    "http://" + domainName,
					Headers: map[string]string{
						"Authorization": "Basic " + payload,
					},
				},
			)
			if err != nil {
				return "", err
			}
			if status != 200 {
				t.Fatal("status code is not ok. expected 200, got ", status, "body:", output)
			}
			actualHostname, err := setup.ParseEchoHostname(output)
			if err != nil {
				t.Fatal(err)
			}
			if actualHostname != "echoserver-1" {
				t.Fatal("unexpected hostname ", actualHostname, " was expecting echoserver-1")
			}
			return "", nil
		},
	)
}
