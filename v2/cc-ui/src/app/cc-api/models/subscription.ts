/* tslint:disable */
export interface Subscription {
  channelAddress?: string;
  channelId?: string;
  channelType?: 'FLOCK' | 'SLACK';
  filters?: {[key: string]: Array<string>};
  id?: string;
  notificationSubject?: string;
  notificationType?: 'APP_DEPLOYMENT' | 'QASUITE_SANITY' | 'DR_RESULT' | 'STACK_SIGNOFF' | 'ALERT';
  stackName?: string;
}
