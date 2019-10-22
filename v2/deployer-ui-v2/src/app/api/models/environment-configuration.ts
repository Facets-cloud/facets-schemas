/* tslint:disable */
import { K8sLoggingConfiguration } from './k8s-logging-configuration';
import { ExternalDnsConfiguration } from './external-dns-configuration';
import { S3DumpAwsConfig } from './s3dump-aws-config';
import { SSLConfigs } from './sslconfigs';
export interface EnvironmentConfiguration {
  commonConfigs?: {[key: string]: string};
  commonCredentials?: {[key: string]: string};
  ecrMirrorRepo?: string;
  k8sLoggingConfiguration?: K8sLoggingConfiguration;
  kubernetesApiEndpoint?: string;
  kubernetesToken?: string;
  nodeGroup?: string;
  privateDnsConfiguration?: ExternalDnsConfiguration;
  publicDnsConfiguration?: ExternalDnsConfiguration;
  s3DumpAwsConfig?: S3DumpAwsConfig;
  sslConfigs?: SSLConfigs;
}
