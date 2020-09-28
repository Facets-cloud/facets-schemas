/* tslint:disable */
export interface DeploymentLog {
  buildSummary?: {};
  codebuildId?: string;
  createdOn?: string;
  description?: string;
  id?: string;
  releaseType?: 'HOTFIX' | 'RELEASE';
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
}
