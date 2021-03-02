/* tslint:disable */
import { TeamResource } from './team-resource';
export interface Team {
  id?: string;
  name?: string;
  notificationChannels?: {[key: string]: string};
  resources?: Array<TeamResource>;
}
