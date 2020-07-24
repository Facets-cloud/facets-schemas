/* tslint:disable */
import { Condition } from './condition';
export interface TestBuildDetails {
  applicationFamily?: string;
  applicationId?: string;
  branch?: string;
  branchType?: string;
  buildId?: string;
  id?: string;
  prId?: string;
  sonarUrl?: string;
  testStatus?: 'PASS' | 'FAIL' | 'TEST_FAILED' | 'INPROGRESS';
  testStatusRules?: Array<Condition>;
  timestamp?: number;
}
