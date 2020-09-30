/* tslint:disable */
import { TerraformChange } from './terraform-change';
import { DeploymentContext } from './deployment-context';
export interface DeploymentLog {
  buildSummary?: {};
  changesApplied?: Array<TerraformChange>;
  codebuildId?: string;
  createdOn?: string;
  deploymentContext?: DeploymentContext;
  description?: string;
  id?: string;
  releaseType?: 'HOTFIX' | 'RELEASE';
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
}
