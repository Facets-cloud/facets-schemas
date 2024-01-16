package setup

import (
	"testing"

	"github.com/gruntwork-io/terratest/modules/k8s"
)

const blueprintPath = "./intent.json"

func TestIngressSetupPossible(t *testing.T) {
	CreateFromBlueprint(t, blueprintPath)

	// now call the destroy part
	// DeleteFromBlueprint(t, blueprintPath)
}

func TestIngressDestroy(t *testing.T) {
	DeleteFromBlueprint(t, blueprintPath)

	// namespace is deleted here because deleting namespace with echoservers
	// also deletes the entire ingress setup.
	// intentionally ignoring error
	k8s.DeleteNamespaceE(t, &k8s.KubectlOptions{}, NAMESPACE)
}
