/* tslint:disable */
export interface Build {
  applicationId?: string;
  codeBuildId?: string;
  environmentVariable?: {[key: string]: string};
  id?: string;
  image?: string;
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
  tag?: string;
  timestamp?: number;
}
