pipeline{
    agent{
        kubernetes {
            yamlFile 'go-test.yaml'
        }
    }
    stages{
        stage("Terratest unit test in tfdev branch"){
            when {
                expression {
                    env.BRANCH_NAME == 'tfdev'
                }
            }
            steps{
                container('go-test'){
                    sh '''
                    echo "========Intitaing the go test suite inside branch = tf-dev========"
                    go test ./... -v 
                    '''
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