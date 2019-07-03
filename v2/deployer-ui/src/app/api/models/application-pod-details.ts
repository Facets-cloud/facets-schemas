/* tslint:disable */
import { PodResource } from './pod-resource';
export interface ApplicationPodDetails {
  creationTimestamp?: string;
  image?: string;
  imageID?: string;
  labels?: {[key: string]: string};
  name?: string;
  podStatus?: string;
  ready?: boolean;
  resourceUsage?: PodResource;
}
