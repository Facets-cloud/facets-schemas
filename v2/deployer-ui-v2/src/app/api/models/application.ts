/* tslint:disable */
import { HealthCheck } from './health-check';
import { Port } from './port';
import { PVC } from './pvc';
export interface Application {
  dnsType?: 'PUBLIC' | 'PRIVATE';
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationType?: 'SERVICE' | 'SCHEDULED_JOB' | 'STATEFUL_SET';
  buildType?: 'MVN' | 'FREESTYLE_DOCKER' | 'DOTNET_CORE' | 'MVN_IONIC' | 'JDK6_MAVEN2' | 'MJ_NUGET';
  ciEnabled?: boolean;
  commonConfigs?: {[key: string]: string};
  deploymentStrategy?: 'Recreate' | 'RollingUpdate';
  dnsPrefix?: string;
  applicationRootDirectory?: string;
  healthCheck?: HealthCheck;
  id?: string;
  loadBalancerType?: 'INTERNAL' | 'EXTERNAL';
  name?: string;
  ports?: Array<Port>;
  pvcList?: Array<PVC>;
  repositoryUrl?: string;
  vcsProvider?: 'BITBUCKET' | 'GITHUB';
}
