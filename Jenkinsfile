pipeline{
    agent{
        kubernetes {
            yamlFile 'go-test.yaml'
        }
    }
    parameters {
        choice(name: 'Env', choices: ['dev', 'qa', 'prod'], description: 'Choose the environment you want to deploy')
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
                        container('go-test'){
                            sh '''
                            echo "========formatting the tf provider code created by jenkins========"
                            terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/provider.tf
                            echo "========Intitaing the go test suite inside branch = tf-dev========"
                            go test ./... -v 
                            '''
                        }
                    } else if (params.Env == 'qa') {
                        echo 'Using the qa configurations from secrets'
                        withCredentials([
                            file(credentialsId: 'qa-kubeconfig', variable: 'KUBECONFIG')
                        ]){
                            gv.createKubernetesProvider()
                            container('go-test'){
                                sh '''
                                echo "========formatting the tf provider code created by jenkins========"
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/provider.tf
                                echo "========Intitaing the go test suite inside branch = tf-dev========"
                                go test ./... -v 
                                '''
                            }
                        }
                        
                    } else if (params.Env == 'prod') {
                        echo 'Using the production configurations from secrets'
                        withCredentials([
                            file(credentialsId: 'prod-kubeconfig', variable: 'KUBECONFIG')
                        ]){
                            gv.createKubernetesProvider()
                            container('go-test'){
                                sh '''
                                echo "========formatting the tf provider code created by jenkins========"
                                terraform fmt ${WORKSPACE}/capillary-cloud-tf/modules/provider.tf
                                echo "========Intitaing the go test suite inside branch = tf-dev========"
                                go test ./... -v 
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