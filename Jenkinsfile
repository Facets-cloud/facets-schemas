pipeline{
    agent{
        kubernetes {
            yamlFile 'go-test.yaml'
        }
    }
    environment {
        AWS_REGION = "us-east-1"
        PROFILE = "default"
        GCP_PROJECT_REGION = "asia-south1"
        GCP_PROJECT_NAME = "facets-cp-test"
    }
    parameters {
        string(name: "go_test_path", trim: true)

    }
    stages{
        stage("init") {
            steps {
                script {
                   gv = load "provider.groovy" 
                }
            }
        }
        stage("Terratest unit test in PR"){
            when {
                expression {
                    env.BRANCH_NAME.startsWith('PR')
                }
            }
            steps{
                script {
                    echo 'Using the configurations from incluster'
                    withCredentials([[
                        $class: 'AmazonWebServicesCredentialsBinding',
                        credentialsId: "facets-jenkins-aws-creds",
                        accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                        secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                    ],file(credentialsId: 'aws_kubeconfig', variable: 'AWS_KUBECONFIG'),
                    file(credentialsId: 'gcp_kubeconfig', variable: 'GCP_KUBECONFIG'),
                    file(credentialsId: 'gcp_json', variable: 'GCP_CREDENTIALS'),
                    string(credentialsId: 'azure_client_id', variable: 'AZURE_CLIENT_ID'),string(credentialsId: 'azure_tenant_id', variable: 'AZURE_TENANT_ID'), string(credentialsId: 'azure_client_secret', variable: 'AZURE_CLIENT_SECRET'),
                    string(credentialsId: 'azure_subscription_id', variable: 'AZURE_SUBSCRIPTION_ID'),file(credentialsId: 'azure_kubeconfig', variable: 'AZURE_KUBECONFIG')]){
                        container('go-test'){
                        sh """
                        echo "========Intitaing the go test suite inside PR========"
                        aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
                        aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
                        aws configure set default.region ${AWS_REGION}
                        aws configure set default.output json
                        AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} AWS_KUBECONFIG=${AWS_KUBECONFIG} AWS_REGION=${AWS_REGION} PROFILE=${PROFILE} GCP_KUBECONFIG=${GCP_KUBECONFIG} GCP_CREDENTIALS=${GCP_CREDENTIALS} AZURE_CLIENT_ID=${AZURE_CLIENT_ID} AZURE_TENANT_ID=${AZURE_TENANT_ID} AZURE_SUBSCRIPTION_ID=${AZURE_SUBSCRIPTION_ID} AZURE_KUBECONFIG=${AZURE_KUBECONFIG} go test ${go_test_path} -v -timeout 120m 
                        """
                        }
                    } 
                }
            }
        }
        stage("Terratest unit test in TFDEV"){
            when {
                expression {
                    env.BRANCH_NAME == "tfdev"
                }
            }
            steps{
                script {
                    echo 'Using the configurations from incluster'
                    withCredentials([[
                        $class: 'AmazonWebServicesCredentialsBinding',
                        credentialsId: "facets-jenkins-aws-creds",
                        accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                        secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                    ],file(credentialsId: 'aws_kubeconfig', variable: 'AWS_KUBECONFIG'),
                    file(credentialsId: 'gcp_kubeconfig', variable: 'GCP_KUBECONFIG'),
                    file(credentialsId: 'gcp_json', variable: 'GCP_CREDENTIALS'),
                    string(credentialsId: 'azure_client_id', variable: 'AZURE_CLIENT_ID'),string(credentialsId: 'azure_tenant_id', variable: 'AZURE_TENANT_ID'),
                    string(credentialsId: 'azure_subscription_id', variable: 'AZURE_SUBSCRIPTION_ID'),file(credentialsId: 'azure_kubeconfig', variable: 'AZURE_KUBECONFIG')]){
                        container('go-test'){
                        sh """
                        echo "========Intitaing the go test suite inside PR========"
                        aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
                        aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
                        aws configure set default.region ${AWS_REGION}
                        aws configure set default.output json
                        AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} AWS_KUBECONFIG=${AWS_KUBECONFIG} AWS_REGION=${AWS_REGION} PROFILE=${PROFILE} GCP_KUBECONFIG=${GCP_KUBECONFIG} GCP_CREDENTIALS=${GCP_CREDENTIALS} AZURE_CLIENT_ID=${AZURE_CLIENT_ID} AZURE_TENANT_ID=${AZURE_TENANT_ID} AZURE_SUBSCRIPTION_ID=${AZURE_SUBSCRIPTION_ID} AZURE_KUBECONFIG=${AZURE_KUBECONFIG} go test ${go_test_path} -v -timeout 120m 
                        """
                        }
                    } 
                }
            }
        }
    }
    post{
        success{
            echo "========pipeline executed successfully ========"
        }
        failure{
            echo "========pipeline execution failed========"
        }
    }
}