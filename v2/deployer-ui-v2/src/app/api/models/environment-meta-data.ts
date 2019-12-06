/* tslint:disable */
export interface EnvironmentMetaData {
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  environmentType?: 'QA' | 'PRODUCTION';
  gitFlowDevelopmentBranchDeploymentAllowed?: boolean;
  name?: string;
}
