package misctest

import (
	"crypto/tls"
	"errors"
	"fmt"
	"net/http"
	"strings"
	"testing"
	"time"

	"github.com/Facets-cloud/facets-iac/tests/nginx-ingress-tests/setup"
	http_helper "github.com/gruntwork-io/terratest/modules/http-helper"
	"github.com/gruntwork-io/terratest/modules/logger"
	"github.com/gruntwork-io/terratest/modules/retry"
)

func TestSSLRedirectWorks(t *testing.T) {
	const (
		before = "ssl_redirect_before_intent.json"
		after  = "ssl_redirect_after_intent.json"
	)

	// initialise with the before intent.json first
	terraformOptions := setup.CreateFromBlueprint(t, before)
	terraformOutput := setup.ParseOutput(t, terraformOptions)

	domainName, err := setup.GetDomainName(terraformOutput, "1")
	if err != nil {
		t.Fatal(err)
	}

	// Wait for dns
	setup.WaitUntilDnsExists(t, domainName)

	// assert that http response is 200 and from echoserver-1
	http_helper.HttpGetWithRetryWithCustomValidation(
		t,
		"http://"+domainName,
		&tls.Config{},
		10, 10*time.Second,
		setup.MakeHostnameValidator(
			setup.PathHostnameLink{ExpectedHostname: "echoserver-1", ExpectedStatusCode: 200},
		),
	)

	// now enable https redirect
	setup.CreateFromBlueprint(t, after)

	// Wait for dns
	setup.WaitUntilDnsExists(t, domainName)

	// check the status code
	// 	done this way because http_helper does not have a "dont follow redirect" option
	output := retry.DoWithRetry(
		t,
		"Check Redirect Status Code",
		10, 10*time.Second,
		func() (string, error) {
			client := &http.Client{
				CheckRedirect: func(req *http.Request, via []*http.Request) error {
					return http.ErrUseLastResponse
				},
			}
			res, err := client.Get("http://" + domainName)
			if err != nil {
				return "", err
			}

			// redirect status code should be 308 (could be changed to include 300-308 range)
			if res.StatusCode != 308 {
				return "", errors.New(fmt.Sprint("response code is: ", res.StatusCode, " expected 308"))
			}

			locationHeaderUrl := res.Header.Get("Location")
			if !strings.HasPrefix(locationHeaderUrl, "https://") {
				return "", errors.New(fmt.Sprint("redirect is not to https", res.Header))
			}
			return fmt.Sprint("redirect url is: ", locationHeaderUrl), nil
		},
	)
	logger.Log(t, output)
	// assert that https response is echoserver-1
	logger.Log(t, "Check HTTPS response is from echoserver-1")
	http_helper.HttpGetWithRetryWithCustomValidation(
		t,
		"http://"+domainName,
		&tls.Config{InsecureSkipVerify: true},
		10, 10*time.Second,
		setup.MakeHostnameValidator(
			setup.PathHostnameLink{ExpectedHostname: "echoserver-1"},
		),
	)

}
