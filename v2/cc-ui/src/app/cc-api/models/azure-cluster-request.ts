/* tslint:disable */
import { TimeZone } from './time-zone';
export interface AzureClusterRequest {
  autoSignOffSchedule?: string;
  cdPipelineParent?: string;
  clientId?: string;
  clientSecret?: string;
  cloud?: 'AWS' | 'AZURE';
  clusterName?: string;
  clusterVars?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  k8sRequestsToLimitsRatio?: number;
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  requireSignOff?: boolean;
  schedules?: {[key: string]: string};
  stackName?: string;
  subscriptionId?: string;
  tenantId?: string;
  tz?: TimeZone;
}
