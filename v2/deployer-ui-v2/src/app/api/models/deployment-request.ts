/* tslint:disable */
export interface DeploymentRequest {
  extraEnv?: {[key: string]: string};
  id?: string;
  overrideBuildSteps?: Array<string>;
  overrideCCVersion?: string;
  releaseType?: 'HOTFIX' | 'RELEASE';
  tag?: string;
  triggeredBy?: string;
}
