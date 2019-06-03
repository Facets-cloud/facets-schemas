/* tslint:disable */
export interface Deployment {
  applicationId?: string;
  configurations?: {[key: string]: string};
  environment?: string;
  id?: string;
  image?: string;
  podSize?: 'SMALL' | 'LARGE' | 'XLARGE';
  rollbackEnabled?: boolean;
  timestamp?: string;
}
