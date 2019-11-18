/* tslint:disable */
import { K8sLoggingConfiguration } from './k8s-logging-configuration';
import { Kube2IamConfiguration } from './kube-2iam-configuration';
import { ExternalDnsConfiguration } from './external-dns-configuration';
import { S3DumpAwsConfig } from './s3dump-aws-config';
import { SSLConfigs } from './sslconfigs';
export interface EnvironmentConfiguration {
  kubernetesToken?: string;
  commonConfigs?: {[key: string]: string};
  ecrMirrorRepo?: string;
  k8sLoggingConfiguration?: K8sLoggingConfiguration;
  kube2IamConfiguration?: Kube2IamConfiguration;
  kubernetesApiEndpoint?: string;
  commonCredentials?: {[key: string]: string};
  nodeGroup?: string;
  privateDnsConfiguration?: ExternalDnsConfiguration;
  publicDnsConfiguration?: ExternalDnsConfiguration;
  s3DumpAwsConfig?: S3DumpAwsConfig;
  spotTerminationHandlingEnabled?: boolean;
  sslConfigs?: SSLConfigs;
}
