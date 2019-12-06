/* tslint:disable */
import { ApplicationAction } from './application-action';
import { Exception } from './exception';
export interface ActionExecution {
  action?: ApplicationAction;
  triggerException?: Exception;
  triggerStatus?: 'SUCCESS' | 'FAILURE';
  triggerTime?: number;
}
