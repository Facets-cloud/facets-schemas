/* tslint:disable */
import { ClusterAutoscalerConfiguration } from './cluster-autoscaler-configuration';
import { K8sLoggingConfiguration } from './k8s-logging-configuration';
import { Kube2IamConfiguration } from './kube-2iam-configuration';
import { ExternalDnsConfiguration } from './external-dns-configuration';
import { S3DumpAwsConfig } from './s3dump-aws-config';
import { SSLConfigs } from './sslconfigs';
export interface EnvironmentConfiguration {
  clusterAutoscalerConfiguration?: ClusterAutoscalerConfiguration;
  commonConfigs?: {[key: string]: string};
  commonCredentials?: {[key: string]: string};
  ecrMirrorRepo?: string;
  jmxSideCarEnabled?: boolean;
  k8sLoggingConfiguration?: K8sLoggingConfiguration;
  kube2IamConfiguration?: Kube2IamConfiguration;
  kubernetesApiEndpoint?: string;
  kubernetesToken?: string;
  metricServerEnabled?: boolean;
  newRelicClusterName?: string;
  nodeGroup?: string;
  preDeployTaskEnabled?: boolean;
  privateDnsConfiguration?: ExternalDnsConfiguration;
  publicDnsConfiguration?: ExternalDnsConfiguration;
  requestsToLimitsRatio?: number;
  resourceAllocationStrategyDefinition?: {[key: string]: number};
  s3DumpAwsConfig?: S3DumpAwsConfig;
  spotTerminationHandlingEnabled?: boolean;
  sslConfigs?: SSLConfigs;
}
