/* tslint:disable */
import { TimeZone } from './time-zone';
export interface AzureClusterRequest {
  autoSignOffSchedule?: string;
  azs?: Array<string>;
  cdPipelineParent?: string;
  clientId?: string;
  clientSecret?: string;
  cloud?: 'AWS' | 'AZURE' | 'LOCAL';
  clusterName?: string;
  clusterVars?: {[key: string]: string};
  componentVersions?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  instanceTypes?: Array<string>;
  k8sRequestsToLimitsRatio?: number;
  region?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  requireSignOff?: boolean;
  schedules?: {[key: string]: string};
  stackName?: string;
  subscriptionId?: string;
  tenantId?: string;
  tz?: TimeZone;
  vpcCIDR?: string;
}
