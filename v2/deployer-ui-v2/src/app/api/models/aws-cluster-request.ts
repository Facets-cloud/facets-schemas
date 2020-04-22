/* tslint:disable */
import { TimeZone } from './time-zone';
export interface AwsClusterRequest {
  azs?: Array<string>;
  cloud?: 'AWS';
  clusterName?: string;
  externalId?: string;
  region?: 'GovCloud' | 'US_GOV_EAST_1' | 'US_EAST_1' | 'US_EAST_2' | 'US_WEST_1' | 'US_WEST_2' | 'EU_WEST_1' | 'EU_WEST_2' | 'EU_WEST_3' | 'EU_CENTRAL_1' | 'EU_NORTH_1' | 'AP_EAST_1' | 'AP_SOUTH_1' | 'AP_SOUTHEAST_1' | 'AP_SOUTHEAST_2' | 'AP_NORTHEAST_1' | 'AP_NORTHEAST_2' | 'SA_EAST_1' | 'CN_NORTH_1' | 'CN_NORTHWEST_1' | 'CA_CENTRAL_1';
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  roleARN?: string;
  stackName?: string;
  tz?: TimeZone;
}
