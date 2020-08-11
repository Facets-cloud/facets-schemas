/* tslint:disable */
import { Application } from './application';
import { ApplicationMetrics } from './application-metrics';
export interface ApplicationMetricsWrapper {
  application?: Application;
  lastWeekMetrics?: ApplicationMetrics;
  recentMetrics?: ApplicationMetrics;
}
