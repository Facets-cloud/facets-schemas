/* tslint:disable */
import { VariableDetails } from './variable-details';
export interface Stack {
  appPassword?: string;
  clusterVariablesMeta?: {[key: string]: VariableDetails};
  name?: string;
  pauseReleases?: boolean;
  relativePath?: string;
  stackVars?: {[key: string]: string};
  user?: string;
  vcs?: 'GITHUB' | 'BITBUCKET';
  vcsUrl?: string;
}
