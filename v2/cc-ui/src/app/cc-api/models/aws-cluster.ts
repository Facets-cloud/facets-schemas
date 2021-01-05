/* tslint:disable */
export interface AwsCluster {
  autoSignOffSchedule?: string;
  awsRegion?: string;
  azs?: Array<string>;
  cdPipelineParent?: string;
  cloud?: 'AWS';
  clusterMetadata?: {[key: string]: string};
  commonEnvironmentVariables?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  externalId?: string;
  id?: string;
  k8sRequestsToLimitsRatio?: number;
  name?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  requireSignOff?: boolean;
  roleARN?: string;
  schedules?: {[key: string]: string};
  secrets?: {[key: string]: string};
  stackName?: string;
  tz?: string;
  vpcCIDR?: string;
}
