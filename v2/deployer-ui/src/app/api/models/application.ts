/* tslint:disable */
import { Port } from './port';
export interface Application {
  additionalParams?: {[key: string]: string};
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationRootDirectory?: string;
  buildType?: 'MVN' | 'FREESTYLE_DOCKER' | 'DOTNET_CORE';
  dnsPrefix?: string;
  dnsType?: 'PUBLIC' | 'PRIVATE';
  id?: string;
  loadBalancerType?: 'INTERNAL' | 'EXTERNAL';
  name?: string;
  ports?: Array<Port>;
  repositoryUrl?: string;
  vcsProvider?: 'BITBUCKET' | 'GITHUB';
}
