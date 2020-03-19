/* tslint:disable */
export interface Stack {
  name?: string;
  status?: 'DRAFT';
  vcs?: 'GITHUB' | 'BITBUCKET';
  vcsUrl?: string;
}
