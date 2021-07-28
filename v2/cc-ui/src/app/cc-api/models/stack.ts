/* tslint:disable */
import { VariableDetails } from './variable-details';
import { Resource } from './resource';
export interface Stack {
  appPassword?: string;
  artifactories?: Array<string>;
  childStacks?: Array<string>;
  clusterVariablesMeta?: {[key: string]: VariableDetails};
  componentVersions?: {[key: string]: string};
  name?: string;
  pauseReleases?: boolean;
  providedResources?: Array<Resource>;
  relativePath?: string;
  stackVars?: {[key: string]: string};
  user?: string;
  vcs?: 'GITHUB' | 'BITBUCKET';
  vcsUrl?: string;
}
