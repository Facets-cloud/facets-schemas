/* tslint:disable */
export interface QASuiteResult {
  deploymentId?: string;
  moduleStatusMap?: {[key: string]: string};
  redeployment?: boolean;
  status?: 'NA' | 'SUCCESS' | 'FAILURE' | 'TIMEOUT' | 'CANCELLED' | 'RUNNING';
}
