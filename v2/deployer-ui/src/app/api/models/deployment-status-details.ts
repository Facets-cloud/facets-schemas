/* tslint:disable */
import { ApplicationDeploymentDetails } from './application-deployment-details';
import { ApplicationPodDetails } from './application-pod-details';
import { ApplicationServiceDetails } from './application-service-details';
export interface DeploymentStatusDetails {
  deployment?: ApplicationDeploymentDetails;
  pods?: Array<ApplicationPodDetails>;
  service?: ApplicationServiceDetails;
}
