/* tslint:disable */
export interface Build {
  applicationId?: string;
  codeBuildId?: string;
  description?: string;
  environmentVariables?: {[key: string]: string};
  id?: string;
  image?: string;
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
  tag?: string;
  timestamp?: number;
  triggeredBy?: string;
}
