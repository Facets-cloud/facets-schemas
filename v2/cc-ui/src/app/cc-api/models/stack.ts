/* tslint:disable */
import { VariableDetails } from './variable-details';
export interface Stack {
  appPassword?: string;
  artifactories?: Array<string>;
  childStacks?: Array<string>;
  clusterVariablesMeta?: {[key: string]: VariableDetails};
  componentVersions?: {[key: string]: string};
  name?: string;
  pauseReleases?: boolean;
  relativePath?: string;
  stackVars?: {[key: string]: string};
  user?: string;
  vcs?: 'GITHUB' | 'BITBUCKET';
  vcsUrl?: string;
}
