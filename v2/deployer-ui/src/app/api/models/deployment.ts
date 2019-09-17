/* tslint:disable */
import { EnvironmentVariable } from './environment-variable';
import { HPA } from './hpa';
export interface Deployment {
  environment?: string;
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  buildId?: string;
  configurations?: Array<EnvironmentVariable>;
  configurationsMap?: {[key: string]: string};
  deployedBy?: string;
  applicationId?: string;
  horizontalPodAutoscaler?: HPA;
  id?: string;
  podSize?: 'SMALL' | 'LARGE' | 'XLARGE' | 'XXLARGE';
  rollbackEnabled?: boolean;
  timestamp?: string;
}
