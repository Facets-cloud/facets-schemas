/* tslint:disable */
import { TimeZone } from './time-zone';
export interface AwsClusterRequest {
  accessKeyId?: string;
  autoSignOffSchedule?: string;
  azs?: Array<string>;
  cdPipelineParent?: string;
  cloud?: 'AWS';
  clusterName?: string;
  clusterVars?: {[key: string]: string};
  enableAutoSignOff?: boolean;
  externalId?: string;
  instanceTypes?: Array<string>;
  k8sRequestsToLimitsRatio?: number;
  region?: 'GovCloud' | 'US_GOV_EAST_1' | 'US_EAST_1' | 'US_EAST_2' | 'US_WEST_1' | 'US_WEST_2' | 'EU_WEST_1' | 'EU_WEST_2' | 'EU_WEST_3' | 'EU_CENTRAL_1' | 'EU_NORTH_1' | 'AP_EAST_1' | 'AP_SOUTH_1' | 'AP_SOUTHEAST_1' | 'AP_SOUTHEAST_2' | 'AP_NORTHEAST_1' | 'AP_NORTHEAST_2' | 'SA_EAST_1' | 'CN_NORTH_1' | 'CN_NORTHWEST_1' | 'CA_CENTRAL_1';
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  requireSignOff?: boolean;
  roleARN?: string;
  schedules?: {[key: string]: string};
  secretAccessKey?: string;
  stackName?: string;
  tz?: TimeZone;
  vpcCIDR?: string;
}
