/* tslint:disable */
import { Endpoint } from './endpoint';
export interface ApplicationServiceDetails {
  creationTimestamp?: string;
  externalEndpoints?: Array<Endpoint>;
  internalEndpoints?: Array<Endpoint>;
  labels?: {[key: string]: string};
  name?: string;
  selectors?: {[key: string]: string};
  serviceType?: 'ClusterIP' | 'NodePort' | 'LoadBalancer' | 'ExternalName';
}
