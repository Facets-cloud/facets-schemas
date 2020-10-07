/* tslint:disable */
export interface AwsCluster {
  awsRegion?: string;
  azs?: Array<string>;
  cdPipelineParent?: string;
  cloud?: 'AWS';
  commonEnvironmentVariables?: {[key: string]: string};
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
