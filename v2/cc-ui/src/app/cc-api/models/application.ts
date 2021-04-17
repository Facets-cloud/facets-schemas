/* tslint:disable */
import { HealthCheck } from './health-check';
import { Port } from './port';
import { PVC } from './pvc';
export interface Application {
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationRootDirectory?: string;
  applicationType?: 'SERVICE' | 'SCHEDULED_JOB' | 'STATEFUL_SET' | 'SERVERLESS';
  branchBuildRepositoryIds?: Array<string>;
  buildType?: 'MVN' | 'JDK11_MAVEN3' | 'JAVA8_LIBRARY' | 'FREESTYLE_DOCKER' | 'DOTNET_CORE' | 'MVN_IONIC' | 'JDK6_MAVEN2' | 'MJ_NUGET' | 'DOTNET_CORE22' | 'DOTNET_CORE3' | 'SBT' | 'NPM' | 'NPM_UI';
  ciEnabled?: boolean;
  commonConfigs?: {[key: string]: string};
  deploymentStrategy?: 'Recreate' | 'RollingUpdate';
  dnsPrefix?: string;
  dnsType?: 'PUBLIC' | 'PRIVATE';
  elbIdleTimeoutSeconds?: number;
  healthCheck?: HealthCheck;
  id?: string;
  loadBalancerType?: 'INTERNAL' | 'EXTERNAL' | 'NONE';
  name?: string;
  newRelicAlertRecipients?: string;
  ports?: Array<Port>;
  pvcList?: Array<PVC>;
  repositoryDefaultBranch?: string;
  repositoryUrl?: string;
  resourceAllocationStrategy?: 'GENERAL_PURPOSE' | 'CPU_INTENSIVE';
  statusCallbackUrl?: string;
  statusCallbackUrls?: Array<string>;
  strictGitFlowModeEnabled?: boolean;
  tagBuildRepositoryIds?: Array<string>;
  vcsProvider?: 'BITBUCKET' | 'GITHUB';
  webhookId?: string;
}
