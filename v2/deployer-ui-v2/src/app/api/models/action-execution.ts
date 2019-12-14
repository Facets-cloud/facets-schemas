/* tslint:disable */
import { ApplicationAction } from './application-action';
export interface ActionExecution {
  action?: ApplicationAction;
  applicationId?: string;
  id?: string;
  output?: string;
  triggerException?: string;
  triggerStatus?: 'SUCCESS' | 'FAILURE';
  triggerTime?: number;
}
