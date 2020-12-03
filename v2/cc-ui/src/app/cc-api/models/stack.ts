/* tslint:disable */
export interface Stack {
  appPassword?: string;
  name?: string;
  pauseReleases?: boolean;
  relativePath?: string;
  user?: string;
  vcs?: 'GITHUB' | 'BITBUCKET';
  vcsUrl?: string;
}
