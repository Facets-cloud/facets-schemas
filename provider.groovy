def createAWSCredentials() {
    sh """
    echo '''
    [default]
    aws_access_key_id = "$AWS_ACCESS_KEY_ID"
    aws_secret_access_key = "$AWS_SECRET_ACCESS_KEY"

    ''' > ${JENKINS_HOME}/.aws/credentials
    """
}
def createAWSConfig() {
    sh """
    echo '''
    [default]
    region = "$AWS_REGION"
    output = json

    ''' > ${JENKINS_HOME}/.aws/config
    """
}

return this
