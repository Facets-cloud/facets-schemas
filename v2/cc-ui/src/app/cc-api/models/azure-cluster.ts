/* tslint:disable */
export interface AzureCluster {
  autoSignOffSchedule?: string;
  cdPipelineParent?: string;
  clientId?: string;
  clientSecret?: string;
  cloud?: 'AWS' | 'AZURE';
  commonEnvironmentVariables?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  id?: string;
  k8sRequestsToLimitsRatio?: number;
  name?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  requireSignOff?: boolean;
  schedules?: {[key: string]: string};
  secrets?: {[key: string]: string};
  stackName?: string;
  subscriptionId?: string;
  tenantId?: string;
  tz?: string;
}
