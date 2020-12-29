/* tslint:disable */
import { VariableDetails } from './variable-details';
export interface Substack {
  appPassword?: string;
  artifactPath?: string;
  clusterVariablesMeta?: {[key: string]: VariableDetails};
  name?: string;
  pauseReleases?: boolean;
  relativePath?: string;
  stackVars?: {[key: string]: string};
  user?: string;
  vcs?: 'GITHUB' | 'BITBUCKET';
  vcsUrl?: string;
}
