/* tslint:disable */
export interface AwsCluster {
  awsRegion?: string;
  azs?: Array<string>;
  cloud?: 'AWS';
  externalId?: string;
  id?: string;
  name?: string;
  privateSubnetCIDR?: Array<string>;
  publicSubnetCIDR?: Array<string>;
  roleARN?: string;
  stackName?: string;
  vpcCIDR?: string;
}
