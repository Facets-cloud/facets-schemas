/* tslint:disable */
import { QualityGate } from './quality-gate';
export interface CallbackBody {
  appId?: string;
  prNumber?: string;
  properties?: {[key: string]: string};
  qualityGate?: QualityGate;
  status?: string;
}
