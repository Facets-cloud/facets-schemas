/* tslint:disable */
export interface Build {
  image?: string;
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  codeBuildId?: string;
  description?: string;
  environmentVariables?: {[key: string]: string};
  id?: string;
  applicationId?: string;
  promoted?: boolean;
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
  tag?: string;
  testBuild?: boolean;
  timestamp?: number;
  triggeredBy?: string;
}
