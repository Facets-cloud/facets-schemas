/* tslint:disable */
import { GithubPRBranchRef } from './github-prbranch-ref';
export interface GithubPRField {
  base?: GithubPRBranchRef;
  comments_url?: string;
  head?: GithubPRBranchRef;
  state?: string;
  updated_at?: string;
  url?: string;
}
