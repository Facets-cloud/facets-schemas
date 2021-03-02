/* tslint:disable */
export interface DRResult {
  action?: 'CREATE_SNAPSHOT' | 'DELETE_SNAPSHOT';
  exception?: string;
  instanceName?: string;
  resourceType?: string;
  status?: 'SUCCESS' | 'FAILURE';
}
