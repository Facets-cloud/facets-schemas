/* tslint:disable */
import { EnvironmentVariable } from './environment-variable';
export interface DeploymentRequest {
  extraEnv?: Array<EnvironmentVariable>;
  id?: string;
  overrideBuildSteps?: Array<string>;
  releaseType?: 'HOTFIX' | 'RELEASE';
  tag?: string;
}
