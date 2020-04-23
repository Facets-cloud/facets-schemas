/* tslint:disable */
export interface DeploymentRequest {
  id?: string;
  releaseType?: 'HOTFIX' | 'RELEASE';
  tag?: string;
}
