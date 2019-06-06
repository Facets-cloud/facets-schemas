/* tslint:disable */
import { EnvironmentVariable } from './environment-variable';
export interface Deployment {
  applicationId?: string;
  buildId?: string;
  configurations?: Array<EnvironmentVariable>;
  configurationsMap?: {[key: string]: string};
  environment?: string;
  id?: string;
  podSize?: 'SMALL' | 'LARGE' | 'XLARGE';
  rollbackEnabled?: boolean;
  timestamp?: string;
}
