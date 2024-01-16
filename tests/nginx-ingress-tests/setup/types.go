package setup

import "testing"

// This is the namespace used inside the cluster where echoservers and module's
// resources will be created
const NAMESPACE string = "ingress-testing"

// TestCase represents a single test case for testing terraform output.
// It consists of a name and a function that performs the test.
//
// The main purpose of structuring test cases in this way is to enable the main test method
// to handle resource creation, and the handling and parsing of terraform output.
// This way, each TestCase only needs to compare expected and actual output.
//
// Utilised in domain-based test and path-based test
type TestCase struct {
	Name         string
	TestFunction func(t *testing.T)
}

// PathHostnameLink struct represents a link between a given HTTP URL path and
// the corresponding echo server that should respond to it.
// This struct is used primarily to abstract away the test for the if the right
// echo server is responding to a certain URL path.
//
// The HttpPath field represents the URL path to test.
//
// The ExpectedHostname field represents the hostname of the echo server that is
// expected to serve the response for the given URL path.
//
// The ExpectedStatusCode field represents the HTTP status code that is expected
// in the response from the echo server.
//
// The SkipBody field is a flag that indicates whether the response body should
// be ignored or not.
type PathHostnameLink struct {
	HttpPath           string
	ExpectedHostname   string
	ExpectedStatusCode int
	SkipBody           bool
}
