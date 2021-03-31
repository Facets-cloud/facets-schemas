/* tslint:disable */
import { Alert } from './alert';
export interface Response {
  alerts?: Array<Alert>;
  externalURL?: string;
  groupKey?: string;
  groupLabels?: {[key: string]: string};
  receiver?: string;
  status?: string;
  truncatedAlerts?: number;
  version?: string;
}
