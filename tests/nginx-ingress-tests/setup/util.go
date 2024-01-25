package setup

import (
	"errors"
	"fmt"
	"net"
	"os"
	"testing"
	"time"

	"github.com/Jeffail/gabs"
	"github.com/gruntwork-io/terratest/modules/logger"
	"github.com/gruntwork-io/terratest/modules/retry"
)

// From the terraform output, this method parses "legacy_resource_details" and
// returns the "value" for the specified `index`, which usually are ingress
// domain names
func GetDomainName(terraformOutput *gabs.Container, index string) (string, error) {
	domain, err := terraformOutput.JSONPointer("/legacy_resource_details/value/" + index)
	if err != nil {
		return "", err
	}
	return domain.Path("value").Data().(string), nil
}

// For the given domain name, this function waits for the domain to become
// resolvable. ie, domain -> CNAME -> IP. the domain should eventually resolve
// to an IP address
func WaitUntilDnsExists(t *testing.T, hostname string) {
	output := retry.DoWithRetry(
		t,
		"Waiting for DNS",
		30,             // should be ~5 min
		10*time.Second, //
		func() (string, error) {
			addr, err := net.LookupHost(hostname)
			if err != nil {
				return "", err
			}
			return fmt.Sprint(addr), nil
		},
	)
	logger.Log(t, hostname, " dns entries:", output)
}

// For the given domain name, this function waits for domain to become
// resolvable to an IP and then asserts that IP belongs to private space
func WaitUntilDnsExistsPrivateIP(t *testing.T, hostname string) {
	waitUntilDnsExistsIPCheck(
		t,
		hostname,
		"Waiting for DNS (Private IP)",
		func(ip string) error {
			if !isIPPrivate(ip) {
				return errors.New(fmt.Sprint(ip, " is not a private ip"))
			}
			return nil
		},
	)
}

// For the given domain name, this function waits for domain to become
// resolvable to an IP and then asserts that IP belongs to a resolvable public
// space
func WaitUntilDnsExistsPublicIP(t *testing.T, hostname string) {
	waitUntilDnsExistsIPCheck(
		t,
		hostname,
		"Waiting for DNS (Public IP)",
		func(ip string) error {
			if isIPPrivate(ip) {
				return errors.New(fmt.Sprint(ip, " is not a public ip"))
			}
			return nil
		},
	)

}
func waitUntilDnsExistsIPCheck(t *testing.T, hostname string, message string, checkIPFunc func(ip string) error) {
	output := retry.DoWithRetry(
		t,
		message,
		30,             // should be ~5min
		10*time.Second, //
		func() (string, error) {
			addr, err := net.LookupHost(hostname)
			if err != nil {
				return "", err
			}
			for _, ip := range addr {
				if err := checkIPFunc(ip); err != nil {
					return "", err
				}
			}
			return fmt.Sprint(addr), nil
		},
	)
	logger.Log(t, hostname, " dns entries:", output)
}

// checks if given IP falls under private range, if IP is invalid returns false
func isIPPrivate(ipAddr string) bool {
	ip := net.ParseIP(ipAddr)
	if ip == nil {
		return false
	} else if ip.IsPrivate() || ip.IsLoopback() || ip.IsLinkLocalUnicast() || ip.IsLinkLocalMulticast() {
		return true
	} else {
		return false
	}
}

// Fetches the value for the given env variable, fails the testcase if it does
// not exist or if the value is empty
func GetFromEnv(t *testing.T, key string) string {
	value, present := os.LookupEnv(key)
	if !present {
		t.Fatal("expected env variable ", key, " to be present")
	}
	if value == "" {
		t.Fatal("env variable ", key, " is empty")
	}
	return value
}

// Fetches the ingress version to test from the env variable `INGRESS_VERSION`. Currently output could be "0.1" or "0.2"
func GetIngressVersionToTest(t *testing.T) string {
	return GetFromEnv(t, "INGRESS_VERSION")

}
