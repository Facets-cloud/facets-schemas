/* tslint:disable */
import { Condition } from './condition';
export interface QualityGate {
  conditions?: Array<Condition>;
  status?: string;
}
