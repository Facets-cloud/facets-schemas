/* tslint:disable */
import { Port } from './port';
export interface Application {
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationRootDirectory?: string;
  buildType?: 'MVN' | 'FREESTYLE_DOCKER' | 'DOTNET_CORE';
  id?: string;
  loadBalancerType?: 'INTERNAL' | 'EXTERNAL';
  name?: string;
  ports?: Array<Port>;
  repositoryUrl?: string;
  vcsProvider?: 'BITBUCKET' | 'GITHUB';
}
