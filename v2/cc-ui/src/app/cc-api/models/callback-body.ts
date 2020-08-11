/* tslint:disable */
import { Branch } from './branch';
import { QualityGate } from './quality-gate';
export interface CallbackBody {
  appId?: string;
  applicationFamily?: string;
  branch?: Branch;
  deployerBuildId?: string;
  prNumber?: string;
  properties?: {[key: string]: string};
  qualityGate?: QualityGate;
  status?: string;
}
