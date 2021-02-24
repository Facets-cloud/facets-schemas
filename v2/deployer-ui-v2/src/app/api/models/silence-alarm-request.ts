/* tslint:disable */
export interface SilenceAlarmRequest {
  comment?: string;
  labels?: {[key: string]: string};
  snoozeInHours?: number;
}
