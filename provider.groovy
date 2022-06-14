def createKubernetesProvider() {
    sh """ 
    echo '''
    provider "kubernetes" {
        config_path = "$KUBECONFIG"
    }
    ''' > ${WORKSPACE}/capillary-cloud-tf/modules/provider.tf
    """

}


def createHelmProvider() {
    sh """ 
    echo '''
    provider "helm" {
        kubernetes {
            config_path = "$KUBECONFIG"
            load_config_file = false
        }
        version = "1.3.2"
    }
    ''' >> ${WORKSPACE}/capillary-cloud-tf/modules/provider.tf
    """
}


return this