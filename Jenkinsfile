pipeline{
    agent{
        kubernetes {
            yamlFile 'go-test.yaml'
        }
    }
    parameters {
        choice(name: 'Env', choices: ['dev', 'qa', 'prod'], description: 'Choose the environment you want to deploy')
        choice(name: 'AWS_REGION', choices: ['us-east-1', 'us-west-1', 'ap-south-1'], description: 'Choose the region you want to deploy in AWS')
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
                    env.BRANCH_NAME == 'origin/tfdev'
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
                        ]]) {
                            gv.createAWSProvider(params.AWS_REGION)
                            container('go-test'){
                            sh '''
                            echo "========formatting the tf provider code created by jenkins========"
                            terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/provider.tf
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
                        ], file(credentialsId: 'qa-kubeconfig', variable: 'KUBECONFIG')]){
                            gv.createKubernetesProvider()
                            gv.createAWSProvider(params.AWS_REGION)
                            container('go-test'){
                                sh '''
                                echo "========formatting the tf provider code created by jenkins========"
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/provider.tf
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
                        ], file(credentialsId: 'prod-kubeconfig', variable: 'KUBECONFIG')]){
                            gv.createKubernetesProvider()
                            gv.createAWSProvider(params.AWS_REGION)
                            container('go-test'){
                                sh '''
                                echo "========formatting the tf provider code created by jenkins========"
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/provider.tf
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