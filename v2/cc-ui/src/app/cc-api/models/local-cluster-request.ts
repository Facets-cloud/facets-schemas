/* tslint:disable */
import { TimeZone } from './time-zone';
export interface LocalClusterRequest {
  autoSignOffSchedule?: string;
  cdPipelineParent?: string;
  cloud?: 'AWS' | 'AZURE' | 'LOCAL';
  clusterName?: string;
  clusterVars?: {[key: string]: string};
  componentVersions?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  k8sRequestsToLimitsRatio?: number;
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  requireSignOff?: boolean;
  schedules?: {[key: string]: string};
  stackName?: string;
  tz?: TimeZone;
}
