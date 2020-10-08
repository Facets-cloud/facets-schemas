/* tslint:disable */
import { DeploymentLog } from './deployment-log';
import { Stack } from './stack';
export interface ListDeploymentsWrapper {
  clusterId?: string;
  currentSignedOffDeployment?: DeploymentLog;
  deployments?: Array<DeploymentLog>;
  downStreamClusterNames?: Array<string>;
  stack?: Stack;
}
