/* tslint:disable */
export interface Probe {
  failureThreshold?: number;
  httpCheckEndpoint?: string;
  initialDelaySeconds?: number;
  periodSeconds?: number;
  port?: number;
  successThreshold?: number;
  timeout?: number;
}
