/* tslint:disable */
import { LogEvent } from './log-event';
export interface TokenPaginatedResponseLogEvent {
  logEventList?: Array<LogEvent>;
  nextToken?: string;
}
