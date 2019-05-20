/* tslint:disable */
import { PodReplicationDetails } from './pod-replication-details';
export interface ApplicationDeploymentDetails {
  creationTimestamp?: string;
  labels?: {[key: string]: string};
  name?: string;
  replicas?: PodReplicationDetails;
}
