/* tslint:disable */
import { Build } from './build';
export interface TokenPaginatedResponse {
  build?: Build;
  logEventList?: Array<{}>;
  nextToken?: string;
}
