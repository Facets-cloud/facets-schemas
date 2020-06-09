/* tslint:disable */
import { BitbucketPRBranchRef } from './bitbucket-prbranch-ref';
import { BitbucketPRLinks } from './bitbucket-prlinks';
export interface BitbucketPRField {
  description?: string;
  destination?: BitbucketPRBranchRef;
  id?: number;
  links?: BitbucketPRLinks;
  source?: BitbucketPRBranchRef;
  state?: string;
  type?: string;
  updated_on?: string;
}
