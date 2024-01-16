package misctest

import (
	"crypto/tls"
	"fmt"
	"reflect"
	"strings"
	"testing"
	"time"

	"github.com/Facets-cloud/facets-iac/tests/nginx-ingress-tests/setup"
	http_helper "github.com/gruntwork-io/terratest/modules/http-helper"
	"github.com/gruntwork-io/terratest/modules/k8s"
	"github.com/gruntwork-io/terratest/modules/logger"
	"github.com/gruntwork-io/terratest/modules/retry"
)

const MANIFEST = `
apiVersion: batch/v1
kind: Job
metadata:
  name: ingress-hitter
  namespace: {{NAMESPACE}}
spec:
  ttlSecondsAfterFinished: 100
  template:
    metadata:
      labels:
        app: ingress-hitter
    spec:
      initContainers:
        - name: init-wait-for-dns
          image: alpine
          command: ["/bin/sh", "-c"]
          args:
            - |
              apk add --no-cache bind-tools && \
              timeout 5m sh -c "\
                until nslookup {{DOMAIN}} | grep -E -q 'Address: [0-9\.]+$'
                do
                    echo 'Waiting for DNS to resolve...'
                    sleep 5
                done
                echo \"DNS has resolved. $(nslookup {{DOMAIN}} | grep -E 'Address: [0-9\.]+$')\"
              "

      containers:
        - name: ingress-hitter
          image: alpine
          command: ["/bin/sh", "-c"]
          args:
            - |
              apk add --no-cache curl > /dev/null && \
              curl --connect-timeout 60 \
                   --max-time 10 \
                   --retry 10 \
                   --retry-delay 10 \
                   {{DOMAIN}} \
                   2> /dev/null
      restartPolicy: Never
  backoffLimit: 0
`

func TestPrivateOptionWorks(t *testing.T) {
	const (
		before = "private_test_before_intent.json"
		after  = "private_test_after_intent.json"
	)

	// initialise with the before intent.json first
	terraformOptions := setup.CreateFromBlueprint(t, before)
	terraformOutput := setup.ParseOutput(t, terraformOptions)
	domainName, err := setup.GetDomainName(terraformOutput, "1")
	if err != nil {
		t.Fatal(err)
	}

	// wait for dns propagation to happen
	setup.WaitUntilDnsExists(t, domainName)

	// check url is accessible externally
	http_helper.HttpGetWithRetryWithCustomValidation(
		t,
		"http://"+domainName,
		&tls.Config{},
		10, 10*time.Second,
		setup.MakeHostnameValidator(setup.PathHostnameLink{ExpectedHostname: "echoserver-1"}),
	)

	// delete the ingress controller before creating a private lb
	setup.DestroyIngressController(t, terraformOptions)
	// ====== private = true
	setup.CreateFromBlueprint(t, after)

	defer func() { // cleanup; ensure private ip doesnt interfere with other tests
		setup.DestroyIngressController(t, terraformOptions)

	}()

	// wait for dns propagation
	setup.WaitUntilDnsExists(t, domainName)
	setup.WaitUntilDnsExistsPrivateIP(t, domainName)

	// check internally it is accessible
	// to check if private=true works, create a container and do a curl and get
	// json output
	manifest := strings.Replace(MANIFEST, "{{NAMESPACE}}", setup.NAMESPACE, -1)
	manifest = strings.Replace(manifest, "{{DOMAIN}}", domainName, -1)

	_k8sOptions := &k8s.KubectlOptions{Namespace: setup.NAMESPACE}

	k8s.KubectlApplyFromString(t, _k8sOptions, manifest)
	job := k8s.GetJob(t, _k8sOptions, "ingress-hitter")
	logger.Log(t, "waiting until job succeeds")
	defer printFinalJobStatus(t, _k8sOptions)
	k8s.WaitUntilJobSucceed(t, _k8sOptions, job.Name, 30, 10*time.Second) // should wait 5 min. dns takes ~2 min to resolve

	logs, err := k8s.RunKubectlAndGetOutputE(t, _k8sOptions, "logs", "--tail=-1", "-l app=ingress-hitter", "--container=ingress-hitter")
	if err != nil {
		t.Fatal(err)
	}
	actualHostname, err := setup.ParseEchoHostname(logs)
	if err != nil {
		t.Fatal(err)
	}
	if actualHostname != "echoserver-1" {
		t.Fatal("actual hostname differs from expected hostname. expected echoserver-1 got:", actualHostname)
	}

	// check url **isnt** accessible from outside
	err = http_helper.HttpGetWithRetryWithCustomValidationE(
		t,
		"http://"+domainName,
		&tls.Config{},
		2, 10*time.Second,
		func(statusCode int, body string) bool {
			if statusCode == 200 {
				// did not expect to get a 200 status code
				return false
			}
			logger.Log(t, statusCode, body)
			return true
		},
	)
	if err != nil {
		logger.Log(t, err, reflect.TypeOf(err))
	}
	if _, ok := err.(retry.MaxRetriesExceeded); !ok {
		t.Fatal("error is not 'max retries exceeded'", err)
	}
}

func printFinalJobStatus(t *testing.T, k8sOptions *k8s.KubectlOptions) {
	job, err := k8s.GetJobE(t, k8sOptions, "ingress-hitter")
	if err != nil {
		return
	}
	logger.Log(t, fmt.Sprintf("Final Job Status: %+v", job.Status.Conditions))
}
