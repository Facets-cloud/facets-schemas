/* tslint:disable */
import { DeploymentLog } from './deployment-log';
import { DeploymentsStats } from './deployments-stats';
import { Stack } from './stack';
export interface ListDeploymentsWrapper {
  clusterId?: string;
  currentSignedOffDeployment?: DeploymentLog;
  deployments?: Array<DeploymentLog>;
  deploymentsFull?: Array<DeploymentLog>;
  deploymentsStats?: DeploymentsStats;
  downStreamClusterNames?: Array<string>;
  stack?: Stack;
}
