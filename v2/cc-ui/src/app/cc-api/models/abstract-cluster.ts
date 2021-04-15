/* tslint:disable */
export interface AbstractCluster {
  autoSignOffSchedule?: string;
  cdPipelineParent?: string;
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
  tz?: string;
}
