/* tslint:disable */
export interface Build {
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationId?: string;
  codeBuildId?: string;
  description?: string;
  environmentVariables?: {[key: string]: string};
  id?: string;
  image?: string;
  promoted?: boolean;
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
  tag?: string;
  testBuild?: boolean;
  timestamp?: number;
  triggeredBy?: string;
}
