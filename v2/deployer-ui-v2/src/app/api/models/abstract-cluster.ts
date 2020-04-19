/* tslint:disable */
export interface AbstractCluster {
  cloud?: 'AWS';
  commonEnvironmentVariables?: {[key: string]: string};
  id?: string;
  name?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD';
  stackName?: string;
  tz?: string;
}
