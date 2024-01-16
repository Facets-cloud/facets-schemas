package domainprefixtest

import (
	"crypto/tls"
	"testing"
	"time"

	"github.com/Facets-cloud/facets-iac/tests/nginx-ingress-tests/setup"
	"github.com/Jeffail/gabs"
	http_helper "github.com/gruntwork-io/terratest/modules/http-helper"
	"github.com/gruntwork-io/terratest/modules/terraform"
)

const BLUEPRINT_PATH = "./intent.json"

var terraformOptions *terraform.Options
var terraformOutput *gabs.Container

func TestDomainPrefixTest(t *testing.T) {
	// setup
	terraformOptions = setup.CreateFromBlueprint(t, BLUEPRINT_PATH)
	terraformOutput = setup.ParseOutput(t, terraformOptions)

	// https://pkg.go.dev/testing#hdr-Subtests_and_Sub_benchmarks
	for _, test := range getAllTestCases() {
		ok := t.Run(test.Name, test.TestFunction)
		if !ok {
			t.FailNow()
		}
	}
}

func getAllTestCases() []setup.TestCase {
	return []setup.TestCase{
		{Name: "Domain Prefix works", TestFunction: testBasicDomainPrefixWorks},
		{Name: "Domain Prefix and Path combination", TestFunction: testDomainPrefixPathCombinations},
	}
}

// Takes in a combination of HTTP Path + expected hostname and runs validation on that.
// Ingress index is the "ingress domain" value from terraform outputs json.
func testMultiplePathsDynamic(t *testing.T, ingressIndex string, combinations []setup.PathHostnameLink) {
	ingress1Obj, err := terraformOutput.JSONPointer("/legacy_resource_details/value/" + ingressIndex)
	if err != nil {
		t.Fatal(err)
	}
	domain := "http://" + ingress1Obj.Path("value").Data().(string)
	for _, test := range combinations {
		http_helper.HttpGetWithRetryWithCustomValidation(
			t,
			domain+test.HttpPath,
			&tls.Config{},
			10, 10*time.Second,
			setup.MakeHostnameValidator(test),
		)
	}
}

func testBasicDomainPrefixWorks(t *testing.T) {

	// ingress domain 1 should be "echoserver1.<rest of the domain>"
	testMultiplePathsDynamic(t, "1", []setup.PathHostnameLink{
		// path `/` should be reachable
		{HttpPath: "/", ExpectedHostname: "echoserver-1", ExpectedStatusCode: 200},
	})

	// ingress domain 2 should be "echoserver2.<rest of the domain>"
	testMultiplePathsDynamic(t, "2", []setup.PathHostnameLink{
		// path `/` should be reachable
		{HttpPath: "/", ExpectedHostname: "echoserver-2", ExpectedStatusCode: 200},
	})
}

func testDomainPrefixPathCombinations(t *testing.T) {
	var testCases = []setup.PathHostnameLink{
		// echoserver 3 should be accessible
		{HttpPath: "/echoserver-3", ExpectedHostname: "echoserver-3", ExpectedStatusCode: 200},

		// echoserver 4 should be accessible
		{HttpPath: "/echoserver-3/echoserver-4", ExpectedHostname: "echoserver-4", ExpectedStatusCode: 200},

		// echoserver 5 should be accessible
		{HttpPath: "/echoserver-5", ExpectedHostname: "echoserver-5", ExpectedStatusCode: 200},

		// echoserver 3 sub paths should work
		{HttpPath: "/echoserver-3/abc", ExpectedHostname: "echoserver-3", ExpectedStatusCode: 200},
		{HttpPath: "/echoserver-3/abc/pqr", ExpectedHostname: "echoserver-3", ExpectedStatusCode: 200},

		// echoserver 4 sub paths should work
		{HttpPath: "/echoserver-3/echoserver-4/abc", ExpectedHostname: "echoserver-4", ExpectedStatusCode: 200},
		{HttpPath: "/echoserver-3/echoserver-4/abc/123", ExpectedHostname: "echoserver-4", ExpectedStatusCode: 200},

		// echoserver 5 sub paths should work
		{HttpPath: "/echoserver-5/abc", ExpectedHostname: "echoserver-5", ExpectedStatusCode: 200},
		{HttpPath: "/echoserver-5/abc/pqr", ExpectedHostname: "echoserver-5", ExpectedStatusCode: 200},
	}

	// the path combinations are nested under ingress domain "2" from the outputs
	testMultiplePathsDynamic(t, "2", testCases)
}
