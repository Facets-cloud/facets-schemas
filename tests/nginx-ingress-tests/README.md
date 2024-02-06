# NGINX Ingress Testing

## Overview

The collection of test cases perform an integration/e2e testing of the ingress
module verifying the it's functionalities are working.

The main overview of what each test does could be summarised as:

- Specify the module's intent.json as a file
- Create the module through terraform and get/parse its output (this gives the
  domain name and any other dependant output values you may need)
- Write terratest code to test functionality ( such as a url resolving to the
  expected echoserver [_the echo server image being used returns the container's
  hostname in response_] )
- (Optionally) test if the config flags and options result in the expected
  behavior or not

To make it simple to manage, run and organise all the commands to run them
either locally or in a ci environment, `Taskfile` is being used. More on
Taskfile is covered below

## Structure

```
.
├── domain_prefix_test
├── misc_test
├── nginx_ingress_controller -> ../../capillary-cloud-tf/modules/nginx_ingress_controller
├── path_test
├── scripts
│   └── report_parsing
└── setup
```

#### Test Directories

These are the folders that contain the code used to test them module:

- **`setup`**: This go package contains the helper methods used by other tests
  to create and apply the module from an `intent.json` file. Helper methods that
  can be used in your own custom test are detailed later in this README.

  This package also contains the Setup & Destroy code for echoservers and an
  ingress module with a basic config.

- **`path_test`**: This package contains code to test path-based rules are
  working. ( eg `example.com/path-1` is handled by `echoserver-1` )
- **`domain_prefix_test`**: Similar to path-based tests, this package handles
  all test cases for subdomain/domain-prefix rules, and then runs a combined
  test of path-based & domain-prefix based rules
- **`misc_test`**: This package contains test cases to test out flags and
  options such as `force_ssl_redirection`, `private`, `basic_auth`.\
  These tests have more verbose terratest code unlike the previous 2 packages since
  there isn't a straightforward way to abstract code for the different options.

#### Working Directory

- **`nginx_ingress_controller`**

In the current stage, the tests are run against
`capillary-cloud-tf/modules/nginx_ingress_controller`. A Symlink is created and
committed to the same folder as well, which the Setup/Destroy helper method use
as their working directory.

It is possible in the CI environment to alter the symlink to another ingress
module.

#### Misc

- **`scripts/report_parsing`**

This folder contains a python script to process the resulting test report and
performs some operations on it to make it more legible such as removing the
golang package name prefix from test suites and correcting the reported timings.

## Taskfile

### Intro

Taskfile is used to split up code into different components and allows you to
execute those units only.

A simple demo of Taskfile could be as follows:

```yaml
# Taskfile.yaml
version: "3"
tasks:
  hello:
    cmd: echo hello world
```

which allows you to run on the command line `task hello`

```sh
$ task hello
task: [hello] echo hello world
hello world
```

Going through Taskfile's website should provide more clarity and make the yaml
defined here more self-explanatory

### In the code

These are the tasks defined in `Taskfile.yaml`:

```
$ task --list-all
task: Available tasks for this project:
* all-tests:
* ci:
* ci-destroy:
* ci-setup:
* default:
* destroy:
* destroy-echoserver:
* domain-prefix-test:
* misc-test:
* path-test:
* setup:
* setup-echoserver:
* test-echoserver:
* test-ingress:
* test-ingress-destroy:
* test-ingress-setup:
```

If you go through the file as well, you will see the following pattern: Basic
units/tasks are defined such as:

- tasks to create/destroy echoservers & ingress
- tasks to run each category of tests
- tasks specific for CI only

which are then called on by more generalised tasks, like:

- setup -> setup-echoserver, test-ingress-setup
- all-tests -> path-test, domain-prefix-test, misc-test

## How to Run

### Pre-requisites before running

- having a `KUBECONFIG` defined
- AWS credentials defined in environment variables
- Taskfile installed

### Locally

- To run the setup part
  - `task setup`
- To run all the tests
  - `task all-tests`
  - or specific tests:
    - `task path-test`
    - `task domain-prefix-test`
    - `task misc-test`
- To Destroy:
  - `task destroy`

**Note:** You can run individual tasks if you want to test out certain bits only

### Tip: You can run a specific test case too

To run a specific testcase lets take an example. We only want to run
`TestBasicAuth` in `misc` package.

You need to add in the `go test`'s `-run` flag in the task description:

```diff
  misc-test:
-   cmd: "{{.TEST_CMD}} -timeout 30m"
+   cmd: "{{.TEST_CMD}} -timeout 30m -run TestBasicAuth"
    dir: misc_test
```

### CI Specific

Since in CI, report generation is involved instead of using `go test`, we are
using `gotestsum` which is a wrapper around `go test` and supports report
generation.

In order to not force a hard dependency on `gotestsum`,
[this section](https://github.com/Facets-cloud/facets-iac/blob/3679b98b10f71376538ca4fc910f3bfb8bd247c5/tests/nginx-ingress-tests/Taskfile.yaml#L15-L24)
dynamically uses `gotestsum` if present otherwise `go test`

## Helper Methods

<!-- GODOC BEGIN -->

### Index

- [Constants](#constants)
- [func CreateFromBlueprint\(t \*testing.T, blueprintPath string\) \*terraform.Options](#CreateFromBlueprint)
- [func DeleteFromBlueprint\(t \*testing.T, blueprintPath string\)](#DeleteFromBlueprint)
- [func DestroyIngressController\(t \*testing.T, terraformOptions \*terraform.Options\)](#DestroyIngressController)
- [func GetDomainName\(terraformOutput \*gabs.Container, index string\) \(string, error\)](#GetDomainName)
- [func GetFromEnv\(t \*testing.T, key string\) string](#GetFromEnv)
- [func MakeHostnameValidator\(test PathHostnameLink\) func\(int, string\) bool](#MakeHostnameValidator)
- [func ParseEchoHostname\(body string\) \(hostname string, err error\)](#ParseEchoHostname)
- [func ParseOutput\(t \*testing.T, terraformOptions \*terraform.Options\) \*gabs.Container](#ParseOutput)
- [func WaitUntilDnsExists\(t \*testing.T, hostname string\)](#WaitUntilDnsExists)
- [func WaitUntilDnsExistsPrivateIP\(t \*testing.T, hostname string\)](#WaitUntilDnsExistsPrivateIP)
- [func WaitUntilDnsExistsPublicIP\(t \*testing.T, hostname string\)](#WaitUntilDnsExistsPublicIP)
- [type PathHostnameLink](#PathHostnameLink)
- [type TestCase](#TestCase)

### Constants

<a name="NAMESPACE"></a>This is the namespace used inside the cluster where
echoservers and module's resources will be created

```go
const NAMESPACE string = "ingress-testing"
```

<a name="CreateFromBlueprint"></a>

### func [CreateFromBlueprint](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/setup_ingress_util.go#L17)

```go
func CreateFromBlueprint(t *testing.T, blueprintPath string) *terraform.Options
```

This helper method takes in a blueprint.json's path and does a terraform init &
apply. It also returns you the \`terraformOptions\` used if you want to run
terraform commands in your code again

<a name="DeleteFromBlueprint"></a>

### func [DeleteFromBlueprint](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/setup_ingress_util.go#L28)

```go
func DeleteFromBlueprint(t *testing.T, blueprintPath string)
```

This method does the opposite of \`CreateFromBlueprint\` and deletes all the
ingress resources created. It also deletes resources marked with
\`prevent_destroy\`. The destroy part is called in later Taskfile steps so your
test code may not need to explicitly call this.

<a name="DestroyIngressController"></a>

### func [DestroyIngressController](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/setup_ingress_util.go#L44)

```go
func DestroyIngressController(t *testing.T, terraformOptions *terraform.Options)
```

Destroy's only the ingress controller. Useful when terraform apply will create a
new loadbalancer such as in the case of \`private=true\`. This will help avoid
the cases when dns records are not updating to the new LB's url

<a name="GetDomainName"></a>

### func [GetDomainName](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/util.go#L19)

```go
func GetDomainName(terraformOutput *gabs.Container, index string) (string, error)
```

From the terraform output, this method parses "legacy_resource_details" and
returns the "value" for the specified \`index\`, which usually are ingress
domain names

<a name="GetFromEnv"></a>

### func [GetFromEnv](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/util.go#L116)

```go
func GetFromEnv(t *testing.T, key string) string
```

Fetches the value for the given env variable, fails the testcase if it does not
exist or if the value is empty

<a name="MakeHostnameValidator"></a>

### func [MakeHostnameValidator](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/setup_ingress_util.go#L153)

```go
func MakeHostnameValidator(test PathHostnameLink) func(int, string) bool
```

Factory method for generating custom terratest validation methods which
validates that the echoserver response is generated from the expected echoserver

<a name="ParseEchoHostname"></a>

### func [ParseEchoHostname](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/setup_ingress_util.go#L139)

```go
func ParseEchoHostname(body string) (hostname string, err error)
```

parses the json from the echoserver image and returns the hostname

<a name="ParseOutput"></a>

### func [ParseOutput](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/setup_ingress_util.go#L130)

```go
func ParseOutput(t *testing.T, terraformOptions *terraform.Options) *gabs.Container
```

Parses the terraform output and returns a gabs container so you can query the
json output. You can inspect the output values by calling this method and seeing
the test logs which will have this json printed out

<a name="WaitUntilDnsExists"></a>

### func [WaitUntilDnsExists](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/util.go#L30)

```go
func WaitUntilDnsExists(t *testing.T, hostname string)
```

For the given domain name, this function waits for the domain to become
resolvable. ie, domain \-\> CNAME \-\> IP. the domain should eventually resolve
to an IP address

<a name="WaitUntilDnsExistsPrivateIP"></a>

### func [WaitUntilDnsExistsPrivateIP](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/util.go#L49)

```go
func WaitUntilDnsExistsPrivateIP(t *testing.T, hostname string)
```

For the given domain name, this function waits for domain to become resolvable
to an IP and then asserts that IP belongs to private space

<a name="WaitUntilDnsExistsPublicIP"></a>

### func [WaitUntilDnsExistsPublicIP](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/util.go#L66)

```go
func WaitUntilDnsExistsPublicIP(t *testing.T, hostname string)
```

For the given domain name, this function waits for domain to become resolvable
to an IP and then asserts that IP belongs to a resolvable public space

<a name="PathHostnameLink"></a>

### type [PathHostnameLink](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/types.go#L37-L42)

PathHostnameLink struct represents a link between a given HTTP URL path and the
corresponding echo server that should respond to it. This struct is used
primarily to abstract away the test for the if the right echo server is
responding to a certain URL path.

The HttpPath field represents the URL path to test.

The ExpectedHostname field represents the hostname of the echo server that is
expected to serve the response for the given URL path.

The ExpectedStatusCode field represents the HTTP status code that is expected in
the response from the echo server.

The SkipBody field is a flag that indicates whether the response body should be
ignored or not.

```go
type PathHostnameLink struct {
    HttpPath           string
    ExpectedHostname   string
    ExpectedStatusCode int
    SkipBody           bool
}
```

<a name="TestCase"></a>

### type [TestCase](https://github.com/Facets-cloud/facets-iac/blob/tfdev/tests/nginx-ingress-tests/setup/types.go#L17-L20)

TestCase represents a single test case for testing terraform output. It consists
of a name and a function that performs the test.

The main purpose of structuring test cases in this way is to enable the main
test method to handle resource creation, and the handling and parsing of
terraform output. This way, each TestCase only needs to compare expected and
actual output.

Utilised in domain\-based test and path\-based test

```go
type TestCase struct {
    Name         string
    TestFunction func(t *testing.T)
}
```

Generated by [gomarkdoc](https://github.com/princjef/gomarkdoc)

<!-- GODOC END -->

## Writing your own test case

Writing your own test case follows the basic skeleton as follows:

- create your test case function (anywhere, either a separate package [if it
  makes sense] or in `misc_test` package)
- create the module's intent json(s), following the same name as your
  "`test.go`".
- Create your ingress by calling: `setup.CreateFromBlueprint(t, BLUEPRINT_PATH)`
- and get your terraform output by calling:
  `setup.ParseOutput(t, terraformOptions)`
- (if created a new package) [Adding to Taskfile](#adding-to-taskfile)

a boilerplate would like this:

```go
package your_package_name_test

import (
	"testing"

	"github.com/Facets-cloud/facets-iac/tests/nginx-ingress-tests/setup"
	"github.com/Jeffail/gabs"
	http_helper "github.com/gruntwork-io/terratest/modules/http-helper"
)

const BLUEPRINT = "./BLUEPRINT.json"

func TestSomething(t *testing.T) {
	terraformOptions := setup.CreateFromBlueprint(t, BLUEPRINT)
	terraformOutput := setup.ParseOutput(t, terraformOptions) // this is a gabs container

	// code that tests
}

```

the destroy part is taken care by calling `task destroy` (refer `Taskfile`), or
you can do it manually to by calling
`setup.DeleteFromBlueprint(t, terraformOptions)`

### Notes

- if the assertions you want to check is a series of
  `http path, expectedEchoServer`, then refer to `path_test` package for
  convenience utilities being used
- if those assertions require some extensive go code to be written with lots of
  custom validation, `misc_test` is a good reference of that.

### Adding to Taskfile

The tests are run with the assumption the working directory is the same folder
where the test file is located. This means you can directly use the
'intent.json' file path, as if you're in the same directory.

This part is only needed when you are creating a new folder (aka go package)

To add a new package to Taskfile follow this:

1. Create a task for that test package, example using `misc_test`:
   ```yaml
   misc-test:
     cmd: "{{.TEST_CMD}} -timeout 30m"
     dir: misc_test
   ```
2. Add that task to `all-tests`:
   ```diff
   all-tests:
       cmds:
        - task: path-test
        - task: domain-prefix-test
   +    - task: misc-test
   ```
