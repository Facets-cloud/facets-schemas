Feature: Release Wrapper Scripts
  Background:
    Given script "/sources/primary/capillary-cloud-tf/tfmain/scripts/read_from_sm.py" is mocked
    Given script "/sources/primary/capillary-cloud-tf/tfmain/scripts/iac_migration.py" is mocked
    Given script "/sources/primary/capillary-cloud-tf/tfmain/scripts/generate_release_details.py" is mocked
    Given binary "terraform" is mocked
    Given binary "terraform_1.0" is mocked
    Given binary "curl" is mocked
    Given binary "unzip" is mocked
    Given stack name is "default"
    Given an artifact exists in artifactory "default" with name "myapp1" artifactory_uri "myapp1:1.0" and build id "1"
    Given an artifact exists in artifactory "default" with name "myapp2" artifactory_uri "myapp2:1.0" and build id "1"
  Scenario Outline: Custom command release to verify module invocation
    Given a file exists in substack "mysubstack" at "service/instances/substack-app.json" with content
    """
    {
      "kind": "service",
      "version": "0.1",
      "flavor": "default",
      "spec": {
        "release": {
          "build": {
            "artifactory": "default",
            "name": "myapp2"
          }
        },
        "runtime": {
          "env": {
            "redis_host": "${redis.myredis.out.interfaces.writer.host}",
            "invalid_redis_host": "${redis.mynoredis.out.interfaces.writer.host}"
          }
        }
      }
    }
    """
    # Simple enabled app
    Given a file exists in blueprint at "service/instances/enabled-app.json" with content
    """
    {
      "kind": "service",
      "version": "0.1",
      "flavor": "default",
      "spec": {
        "release": {
          "build": {
            "artifactory": "default",
            "name": "myapp1"
          }
        }
      }
    }
    """


    # Simple disabled app
    Given a file exists in blueprint at "service/instances/disabled-app.json" with content
    """
    {
      "kind": "service",
      "version": "0.1",
      "flavor": "default",
      "disabled": true,
      "spec": {
        "release": {}
      }
    }
    """

    # Redis in non-standard path
    Given a file exists in blueprint at "anywhere/myredis.json" with content
    """
    {
      "kind": "redis",
      "version": "0.1",
      "flavor": "k8s",
      "spec": {
        "release": {}
      }
    }
    """

    # Redis enabled in blueprint but disabled in overrides
    Given a file exists in blueprint at "anywhere/myredis2.json" with content
    """
    {
      "kind": "redis",
      "version": "0.1",
      "flavor": "k8s",
      "spec": {
        "release": {}
      }
    }
    """
    And override exists for "redis" named "myredis2" with content
    """
    {
      "disabled": true
    }
    """

    # Redis enabled in blueprint but disabled in overrides the unintended way
    Given a file exists in blueprint at "anywhere/myredis3.json" with content
    """
    {
      "kind": "redis",
      "version": "0.1",
      "flavor": "k8s",
      "spec": {
        "release": {}
      }
    }
    """
    And override exists for "redis" named "myredis3" with content
    """
    {
      "disabled": "true"
    }
    """

    # Simple substack redis
    Given a file exists in substack "mysubstack" at "redis/instances/mysubstackredis.json" with content
    """
    {
      "kind": "redis",
      "version": "0.1",
      "flavor": "k8s",
      "spec": {
        "release": {}
      }
    }
    """

    # Simple disabled configuration json
    Given a file exists in blueprint at "anywhere/myargoconfig.json" with content
    """
    {
      "kind": "configuration",
      "version": "0.1",
      "for": "argo",
      "disabled": true
    }
    """

    # Simple disabled configuration in substack
    Given a file exists in substack "mysubstack" at "conf/mynotificationconf.json" with content
    """
    {
      "kind": "configuration",
      "version": "0.1",
      "for": "notification",
      "disabled": true
    }
    """

    # Disabled config in substack, Enabled in blueprint
    Given a file exists in blueprint at "anywhere/snapschedulerconf.json" with content
    """
    {
      "kind": "configuration",
      "version": "0.1",
      "for": "snapscheduler",
      "disabled": false
    }
    """

    Given a file exists in substack "mysubstack" at "anywhere/snapschedulerconf.json" with content
    """
    {
      "kind": "configuration",
      "version": "0.1",
      "for": "snapscheduler",
      "disabled": true
    }
    """

    Given cloud is "<cloud>"
    Given release type "CUSTOM"
    Given custom command is
    """
    echo "Hello World <cloud>"
    """
    When release script is run
    Then stdout includes lines
    """
    Hello World <cloud>
    ## INVOKING THE BELOW COMMANDS ##
    """
    # Enabled by default config module
    And level2 modules must include "module.prometheus" with source "../../modules/0_input_config/prometheus"
    # This is included conditionally and blueprint has no schemahero resources
    And level2 modules must not include "module.schemahero"
    # Enabled app
    And level2 modules must include "module.service_enabled-app" with source "<service_module_path>"
    # Disabled app
    And level2 modules must not include "module.service_disabled-app"
    # Enabled redis
    And level2 modules must include "module.redis_myredis" with source "../../modules/1_input_instance/redis"
    # Disabled redis via overrides
    And level2 modules must not include "module.redis_myredis2"
    # Disabled redis via overrides the wrong way
    And level2 modules must not include "module.redis_myredis3"
    # Redis added via substack
    And level2 modules must include "module.redis_mysubstackredis" with source "../../modules/1_input_instance/redis"
    # Config module disabled in blueprint
    And level2 modules must not include "module.argo_rollouts"
    # Config module disabled in substack
    And level2 modules must not include "module.notification"
    # Config module disabled in substack but enabled in blueprint
    And level2 modules must include "module.snapscheduler" with source "../../modules/0_input_config/snapscheduler"
    # Config module enabled automatically as redis intents are present
    And level2 modules must include "module.redis_exporter" with source "../../modules/0_input_config/redis_exporter"
    # Artifacts.json created correctly for blueprint app
    And the JSON file at "/sources/primary/capillary-cloud-tf/<tfdir>/artifacts.json" must contain "myapp1:1.0" at JSON path "$.enabled-app.artifactUri"
    # Artifacts.json created correctly for substack app
    And the JSON file at "/sources/primary/capillary-cloud-tf/<tfdir>/artifacts.json" must contain "myapp2:1.0" at JSON path "$.substack-app.artifactUri"
    # Verify $ referencing
    And the JSON file at "/sources/primary/capillary-cloud-tf/<tfdir>/level2/input_service_substack-app.tf.json" must contain "${local.module_redis_myredis.interfaces.writer.host}" at JSON path "$.locals.input_service_substack-app.spec.runtime.env.redis_host"
    # Verify invalid $ ref handling
    And the JSON file at "/sources/primary/capillary-cloud-tf/<tfdir>/level2/input_service_substack-app.tf.json" must contain "<EMPTY>" at JSON path "$.locals.input_service_substack-app.spec.runtime.env.invalid_redis_host"
    Examples: clouds
      | cloud | service_module_path | tfdir |
      | AWS   | ../../modules/1_input_instance/application_aws | tfaws |
      | AZURE | ../../modules/1_input_instance/application_azure | tfazure |
      | GCP   | ../../modules/1_input_instance/application_gcp | tfmain |