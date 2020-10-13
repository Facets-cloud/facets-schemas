/* tslint:disable */
import { AppDeployment } from './app-deployment';
import { TerraformChange } from './terraform-change';
export interface DeploymentLog {
  appDeployments?: Array<AppDeployment>;
  changesApplied?: Array<TerraformChange>;
  codebuildId?: string;
  createdOn?: string;
  deploymentType?: 'REGULAR' | 'CUSTOM' | 'ROLLBACK';
  description?: string;
  errorLogs?: Array<string>;
  id?: string;
  releaseType?: 'HOTFIX' | 'RELEASE';
  signedOff?: boolean;
  stackVersion?: string;
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
  tfVersion?: string;
}
