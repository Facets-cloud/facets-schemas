/* tslint:disable */
import { GithubPRField } from './github-prfield';
export interface GithubPREvent {
  action?: string;
  number?: number;
  pull_request?: GithubPRField;
}
