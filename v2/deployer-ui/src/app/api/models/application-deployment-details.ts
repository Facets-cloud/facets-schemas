/* tslint:disable */
import { PodReplicationDetails } from './pod-replication-details';
export interface ApplicationDeploymentDetails {
  creationTimestamp?: string;
  credentialsList?: Array<string>;
  environmentConfigs?: {[key: string]: string};
  labels?: {[key: string]: string};
  name?: string;
  replicas?: PodReplicationDetails;
}
