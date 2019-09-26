/* tslint:disable */
import { Probe } from './probe';
export interface HealthCheck {
  livenessProbe?: Probe;
  readinessProbe?: Probe;
}
