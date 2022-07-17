pipeline{
    agent{
        kubernetes {
            yamlFile 'go-test.yaml'
        }
    }
    parameters {
        choice(name: 'Env', choices: ['dev', 'qa', 'prod'], description: 'Choose the environment you want to deploy')
        choice(name: 'AWS_REGION', choices: ['us-east-1', 'us-west-1', 'ap-south-1'], description: 'Choose the region you want to deploy in AWS')
        string( defaultValue: 'facets-cp-test', name: 'gcp_project_name', trim: true)
        string( defaultValue: 'asia-south1', name: 'gcp_project_region', trim: true)
        credentials(name: 'aws_kubeconfig', credentialType: 'org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl', defaultValue: '', description: 'This is the aws kubeconfig you pass if you have to test in another cluster', required: true)
        credentials(name: 'gcp_kubeconfig', credentialType: 'org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl', defaultValue: '', description: 'This is the gcp kubeconfig you pass if you have to test in another cluster', required: true)
        credentials(name: 'gcp_json', credentialType: 'org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl', defaultValue: '', description: 'This is the json credentials for gcp', required: true)

    }
    stages{
        stage("init") {
            steps {
                script {
                   gv = load "provider.groovy" 
                }
            }
        }
        stage("Terratest unit test in tfdev branch"){
            when {
                expression {
                    env.BRANCH_NAME == 'feature/GCP-ALB'
                }
            }
            steps{
                script {
                    if (params.Env == 'dev') {
                        echo 'Using the dev configurations from incluster'
                        withCredentials([[
                            $class: 'AmazonWebServicesCredentialsBinding',
                            credentialsId: "facets-jenkins-aws-creds",
                            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                        ],file(credentialsId: 'aws_kubeconfig', variable: 'AWS_KUBECONFIG'),
                        file(credentialsId: 'gcp_kubeconfig', variable: 'GCP_KUBECONFIG'),
                        file(credentialsId: 'gcp_json', variable: 'CREDENTIALS')]) {
                            // AWS provider updates
                            gv.createAWSProvider(params.AWS_REGION)
                            // GCP provider updates
                            gv.createGCPProvider(params.AWS_REGION, params.gcp_project_name, params.gcp_project_region)
                            container('go-test'){
                            sh '''
                            echo "========formatting the tf provider code created by jenkins========"
                            terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/aws-provider.tf
                            terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/gcp-provider.tf
                            terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/azure-provider.tf
                            echo "========Intitaing the go test suite inside branch = tf-dev========"
                            go test ./... -v -timeout 30m 
                            '''
                            }
                        }
                        
                    } else if (params.Env == 'qa') {
                        echo 'Using the qa configurations from secrets'
                        withCredentials([[
                            $class: 'AmazonWebServicesCredentialsBinding',
                            credentialsId: "facets-jenkins-aws-creds",
                            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                        ], file(credentialsId: 'aws_kubeconfig', variable: 'AWS_KUBECONFIG'),
                        file(credentialsId: 'gcp_kubeconfig', variable: 'GCP_KUBECONFIG'),
                        file(credentialsId: 'gcp_json', variable: 'CREDENTIALS')]){
                             // AWS provider updates
                            gv.createAWSProvider(params.AWS_REGION)
                            // GCP provider updates
                            gv.createGCPProvider(params.AWS_REGION, params.gcp_project_name, params.gcp_project_region)
                            container('go-test'){
                                sh '''
                                echo "========formatting the tf provider code created by jenkins========"
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/aws-provider.tf
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/gcp-provider.tf
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/azure-provider.tf
                                echo "========Intitaing the go test suite inside branch = tf-dev========"
                                go test ./... -v -timeout 30m 
                                '''
                            }
                        }
                        
                    } else if (params.Env == 'prod') {
                        echo 'Using the production configurations from secrets'
                        withCredentials([[
                            $class: 'AmazonWebServicesCredentialsBinding',
                            credentialsId: "facets-jenkins-aws-creds",
                            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                        ], file(credentialsId: 'aws_kubeconfig', variable: 'AWS_KUBECONFIG'),
                        file(credentialsId: 'gcp_kubeconfig', variable: 'GCP_KUBECONFIG'),
                        file(credentialsId: 'gcp_json', variable: 'CREDENTIALS')]){
                             // AWS provider updates
                            gv.createAWSProvider(params.AWS_REGION)
                            // GCP provider updates
                            gv.createGCPProvider(params.AWS_REGION, params.gcp_project_name, params.gcp_project_region)
                            container('go-test'){
                                sh '''
                                echo "========formatting the tf provider code created by jenkins========"
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/aws-provider.tf
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/gcp-provider.tf
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/azure-provider.tf
                                echo "========Intitaing the go test suite inside branch = tf-dev========"
                                go test ./... -v -timeout 30m 
                                '''
                            }
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