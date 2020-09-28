/* tslint:disable */
export interface AbstractCluster {
  cdPipelineParent?: string;
  cloud?: 'AWS';
  commonEnvironmentVariables?: {[key: string]: string};
  id?: string;
  k8sRequestsToLimitsRatio?: number;
  name?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  schedules?: {[key: string]: string};
  secrets?: {[key: string]: string};
  stackName?: string;
  tz?: string;
}
