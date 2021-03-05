/* tslint:disable */
export interface AwsCluster {
  autoSignOffSchedule?: string;
  awsRegion?: string;
  azs?: Array<string>;
  cdPipelineParent?: string;
  cloud?: 'AWS';
  commonEnvironmentVariables?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  externalId?: string;
  id?: string;
  instanceTypes?: Array<string>;
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
