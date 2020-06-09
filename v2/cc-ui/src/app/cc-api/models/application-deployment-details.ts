/* tslint:disable */
import { HPADetails } from './hpadetails';
import { PodReplicationDetails } from './pod-replication-details';
export interface ApplicationDeploymentDetails {
  creationTimestamp?: string;
  credentialsList?: Array<string>;
  environmentConfigs?: {[key: string]: string};
  hpaStatus?: HPADetails;
  labels?: {[key: string]: string};
  name?: string;
  replicas?: PodReplicationDetails;
}
