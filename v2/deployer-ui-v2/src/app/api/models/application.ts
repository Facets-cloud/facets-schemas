/* tslint:disable */
import { HealthCheck } from './health-check';
import { Port } from './port';
import { PVC } from './pvc';
export interface Application {
  healthCheck?: HealthCheck;
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationType?: 'SERVICE' | 'SCHEDULED_JOB' | 'STATEFUL_SET';
  buildType?: 'MVN' | 'FREESTYLE_DOCKER' | 'DOTNET_CORE' | 'MVN_IONIC' | 'JDK6_MAVEN2' | 'MJ_NUGET';
  ciEnabled?: boolean;
  commonConfigs?: {[key: string]: string};
  deploymentStrategy?: 'Recreate' | 'RollingUpdate';
  dnsPrefix?: string;
  dnsType?: 'PUBLIC' | 'PRIVATE';
  applicationRootDirectory?: string;
  id?: string;
  loadBalancerType?: 'INTERNAL' | 'EXTERNAL';
  name?: string;
  ports?: Array<Port>;
  pvcList?: Array<PVC>;
  repositoryUrl?: string;
  vcsProvider?: 'BITBUCKET' | 'GITHUB';
  webhookId?: string;
}
