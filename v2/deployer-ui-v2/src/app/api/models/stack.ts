/* tslint:disable */
export interface Stack {
  appPassword?: string;
  name?: string;
  relativePath?: string;
  user?: string;
  vcs?: 'GITHUB' | 'BITBUCKET';
  vcsUrl?: string;
}
