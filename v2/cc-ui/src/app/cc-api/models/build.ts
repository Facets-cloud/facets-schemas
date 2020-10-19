/* tslint:disable */
export interface Build {
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationId?: string;
  artifactUrl?: string;
  codeBuildId?: string;
  description?: string;
  environmentVariables?: {[key: string]: string};
  id?: string;
  image?: string;
  promotable?: boolean;
  promoted?: boolean;
  promotionIntent?: 'NA' | 'NOT_CC_ENABLED' | 'HOTFIX' | 'RELEASE';
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
  tag?: string;
  testBuild?: boolean;
  timestamp?: number;
  triggeredBy?: string;
  unpromoted?: boolean;
}
