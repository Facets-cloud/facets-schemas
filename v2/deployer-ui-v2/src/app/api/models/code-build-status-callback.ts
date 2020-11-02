/* tslint:disable */
export interface CodeBuildStatusCallback {
  codebuidId?: string;
  status?: 'SUCCEEDED' | 'FAILED' | 'FAULT' | 'TIMED_OUT' | 'IN_PROGRESS' | 'STOPPED' | 'null';
}
