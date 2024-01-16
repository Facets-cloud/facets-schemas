package pathtest

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

func TestPathBasedRules(t *testing.T) {
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
		{Name: "Echo Server 1 accessible", TestFunction: testEchoServer1Accessible},
		{Name: "Echo Server 2 Basic", TestFunction: testEchoServer2Basic},
		{Name: "Mixed subpath test", TestFunction: testMixedPaths},
	}
}

// Takes in a combination of HTTP Path + expected hostname and runs validation on that.
// Ingress index is the "ingress domain" value from terraform outputs json.
func testMultiplePathsDynamic(t *testing.T, ingressIndex string, combinations []setup.PathHostnameLink) {
	ingress1Obj, err := terraformOutput.JSONPointer("/legacy_resource_details/value/" + ingressIndex)
	if err != nil {
		t.Fatal(err)
	}

	hostname := ingress1Obj.Path("value").Data().(string)
	domain := "http://" + hostname

	setup.WaitUntilDnsExists(t, hostname)

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
func testEchoServer1Accessible(t *testing.T) {
	var testCases = []setup.PathHostnameLink{
		// path `/` should be 404
		{HttpPath: "/", ExpectedHostname: "echoserver-1", ExpectedStatusCode: 404, SkipBody: true},
		// path /echoserver-1 should hit echoserver-1
		{HttpPath: "/echoserver-1", ExpectedHostname: "echoserver-1"},
		// path /echoserver-1/subpath should hit echoserver-1
		{HttpPath: "/echoserver-1/subpath", ExpectedHostname: "echoserver-1"},
		// path /echoserver-1/subpath/subpath2 should hit echoserver-1
		{HttpPath: "/echoserver-1/subpath/subpath2", ExpectedHostname: "echoserver-1"},
	}
	testMultiplePathsDynamic(t, "1", testCases)

}

func testEchoServer2Basic(t *testing.T) {
	var testCases = []setup.PathHostnameLink{
		// path `/` should be 404
		{HttpPath: "/", ExpectedHostname: "echoserver-2", ExpectedStatusCode: 404, SkipBody: true},
		// path /echoserver-1 should hit echoserver-1
		{HttpPath: "/echoserver-2", ExpectedHostname: "echoserver-2"},
		// path /echoserver-1/subpath should hit echoserver-1
		{HttpPath: "/echoserver-2/subpath", ExpectedHostname: "echoserver-2"},
		// path /echoserver-1/subpath/subpath2 should hit echoserver-1
		{HttpPath: "/echoserver-2/subpath/subpath2", ExpectedHostname: "echoserver-2"},
	}
	testMultiplePathsDynamic(t, "2", testCases)
}

func testMixedPaths(t *testing.T) {
	var testCases = []setup.PathHostnameLink{
		// assert echoserver-1 is working
		{HttpPath: "/echoserver-1", ExpectedHostname: "echoserver-1"},
		// assert echoserver-3 is working
		{HttpPath: "/echoserver-1/echoserver-3", ExpectedHostname: "echoserver-3"},

		// assert echoserver-3 subpaths work
		{HttpPath: "/echoserver-1/echoserver-3/subpath", ExpectedHostname: "echoserver-3"},
		// assert echoserver-1 subpaths resolve to echoserver 1
		{HttpPath: "/echoserver-1/echoserver-abcd/subpath", ExpectedHostname: "echoserver-1"},

		// tricky subpath
		// assertion: only the most specific rule is applied
		{HttpPath: "/echoserver-1/echoserver-3-abc4/subpath", ExpectedHostname: "echoserver-3"},
	}
	testMultiplePathsDynamic(t, "3", testCases)
}
