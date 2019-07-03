/* tslint:disable */
export interface Build {
  image?: string;
  applicationId?: string;
  description?: string;
  environmentVariables?: {[key: string]: string};
  id?: string;
  codeBuildId?: string;
  promoted?: boolean;
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
  tag?: string;
  timestamp?: number;
  triggeredBy?: string;
}
