/* tslint:disable */
import { AppDeployment } from './app-deployment';
import { TerraformChange } from './terraform-change';
export interface DeploymentLog {
  appDeployments?: Array<AppDeployment>;
  changesApplied?: Array<TerraformChange>;
  codebuildId?: string;
  createdOn?: string;
  description?: string;
  id?: string;
  releaseType?: 'HOTFIX' | 'RELEASE';
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
}
