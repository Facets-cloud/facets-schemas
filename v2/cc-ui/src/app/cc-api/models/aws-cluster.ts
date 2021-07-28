/* tslint:disable */
export interface AwsCluster {
  accessKeyId?: string;
  autoSignOffSchedule?: string;
  awsRegion?: string;
  azs?: Array<string>;
  cdPipelineParent?: string;
  cloud?: 'AWS' | 'AZURE' | 'LOCAL';
  commonEnvironmentVariables?: {[key: string]: string};
  componentVersions?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  externalId?: string;
  id?: string;
  instanceTypes?: Array<string>;
  k8sRequestsToLimitsRatio?: number;
  name?: string;
  providedVPCId?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD' | 'QA2';
  requireSignOff?: boolean;
  roleARN?: string;
  schedules?: {[key: string]: string};
  secretAccessKey?: string;
  secrets?: {[key: string]: string};
  stackName?: string;
  tz?: string;
  vpcCIDR?: string;
}
