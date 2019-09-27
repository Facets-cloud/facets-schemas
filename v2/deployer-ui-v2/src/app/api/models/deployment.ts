/* tslint:disable */
import { EnvironmentVariable } from './environment-variable';
import { HPA } from './hpa';
export interface Deployment {
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationId?: string;
  buildId?: string;
  configurations?: Array<EnvironmentVariable>;
  configurationsMap?: {[key: string]: string};
  deployedBy?: string;
  environment?: string;
  horizontalPodAutoscaler?: HPA;
  id?: string;
  podSize?: 'SMALL' | 'LARGE' | 'XLARGE' | 'XXLARGE';
  rollbackEnabled?: boolean;
  timestamp?: string;
}
