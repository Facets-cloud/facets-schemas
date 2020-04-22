/* tslint:disable */
export interface AwsCluster {
  awsRegion?: string;
  azs?: Array<string>;
  cloud?: 'AWS';
  commonEnvironmentVariables?: {[key: string]: string};
  externalId?: string;
  id?: string;
  name?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  roleARN?: string;
  stackName?: string;
  tz?: string;
  vpcCIDR?: string;
}
