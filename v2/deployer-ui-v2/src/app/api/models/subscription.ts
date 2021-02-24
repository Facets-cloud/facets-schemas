/* tslint:disable */
export interface Subscription {
  channelAddress?: string;
  channelType?: 'FLOCK';
  filters?: {[key: string]: Array<string>};
  id?: string;
  notificationSubject?: string;
  notificationType?: 'APP_DEPLOYMENT' | 'QASUITE_SANITY' | 'DR_RESULT' | 'STACK_SIGNOFF';
  stackName?: string;
}
