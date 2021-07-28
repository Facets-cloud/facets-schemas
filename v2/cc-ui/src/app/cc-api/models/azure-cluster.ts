/* tslint:disable */
export interface AzureCluster {
  autoSignOffSchedule?: string;
  azs?: Array<string>;
  cdPipelineParent?: string;
  clientId?: string;
  clientSecret?: string;
  cloud?: 'AWS' | 'AZURE' | 'LOCAL';
  commonEnvironmentVariables?: {[key: string]: string};
  componentVersions?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  id?: string;
  instanceTypes?: Array<string>;
  k8sRequestsToLimitsRatio?: number;
  name?: string;
  region?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD' | 'QA2';
  requireSignOff?: boolean;
  schedules?: {[key: string]: string};
  secrets?: {[key: string]: string};
  stackName?: string;
  subscriptionId?: string;
  tenantId?: string;
  tz?: string;
  vpcCIDR?: string;
}
