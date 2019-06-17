/* tslint:disable */
import { HealthCheck } from './health-check';
import { Port } from './port';
export interface Application {
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationRootDirectory?: string;
  buildType?: 'MVN' | 'FREESTYLE_DOCKER' | 'DOTNET_CORE' | 'MVN_IONIC';
  commonConfigs?: {[key: string]: string};
  dnsPrefix?: string;
  dnsType?: 'PUBLIC' | 'PRIVATE';
  healthCheck?: HealthCheck;
  id?: string;
  loadBalancerType?: 'INTERNAL' | 'EXTERNAL';
  name?: string;
  ports?: Array<Port>;
  repositoryUrl?: string;
  vcsProvider?: 'BITBUCKET' | 'GITHUB';
}
