/* tslint:disable */
import { PortMapping } from './port-mapping';
export interface ApplicationServiceDetails {
  creationTimestamp?: string;
  externalEndpoints?: Array<PortMapping>;
  internalEndpoints?: Array<PortMapping>;
  labels?: {[key: string]: string};
  name?: string;
  selectors?: {[key: string]: string};
  serviceType?: 'ClusterIP' | 'NodePort' | 'LoadBalancer' | 'ExternalName';
}
