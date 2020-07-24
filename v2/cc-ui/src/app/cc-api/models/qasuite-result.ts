/* tslint:disable */
export interface QASuiteResult {
  deploymentId?: string;
  id?: string;
  moduleStatusMap?: {[key: string]: string};
  redeployment?: boolean;
  status?: 'NA' | 'SUCCESS' | 'ERROR' | 'FAILURE' | 'TIMEOUT' | 'CANCELLED' | 'RUNNING';
}
