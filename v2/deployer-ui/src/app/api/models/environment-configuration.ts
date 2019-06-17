/* tslint:disable */
import { ExternalDnsConfiguration } from './external-dns-configuration';
export interface EnvironmentConfiguration {
  commonConfigs?: {[key: string]: string};
  commonCredentials?: {[key: string]: string};
  kubernetesApiEndpoint?: string;
  kubernetesToken?: string;
  nodeGroup?: string;
  privateDnsConfiguration?: ExternalDnsConfiguration;
  publicDnsConfiguration?: ExternalDnsConfiguration;
}
