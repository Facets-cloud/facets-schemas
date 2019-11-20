/* tslint:disable */
export interface Port {
  containerPort?: number;
  lbPort?: number;
  name?: string;
  protocol?: 'TCP' | 'HTTP' | 'HTTPS';
}
