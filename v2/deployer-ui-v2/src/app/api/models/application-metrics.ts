/* tslint:disable */
export interface ApplicationMetrics {
  alerts?: number;
  applicationId?: string;
  buildFailures?: number;
  criticalCodeSmells?: number;
  date?: string;
  errors?: number;
  fatalErrorsFromLogs?: number;
  issuesReported?: number;
  outages?: number;
  regressionCoverage?: number;
  regressionFailures?: number;
  responseTime?: number;
  unitTestCoverage?: number;
  unitTests?: number;
}
