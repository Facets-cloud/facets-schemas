package redirecttest

import (
	"testing"

	"github.com/Facets-cloud/facets-iac/tests/nginx-ingress-tests/setup"
)

func TestRedirectsIngressV2(t *testing.T) {
	// skip if ingress version is not 0.2
	if ver := setup.GetIngressVersionToTest(t); ver != "0.2" {
		t.Skip("Skipping redirect tests for ingress version", ver)
	}

	// code that tests
}
