/* tslint:disable */
import { Build } from './build';
import { LogEvent } from './log-event';
export interface TokenPaginatedResponseLogEvent {
  build?: Build;
  logEventList?: Array<LogEvent>;
  nextToken?: string;
}
