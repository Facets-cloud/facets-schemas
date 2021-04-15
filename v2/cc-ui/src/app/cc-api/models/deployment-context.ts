/* tslint:disable */
import { Artifactory } from './artifactory';
import { Artifact } from './artifact';
import { OverrideObject } from './override-object';
import { SnapshotInfo } from './snapshot-info';
export interface DeploymentContext {
  artifactoryDetails?: Array<Artifactory>;
  artifacts?: {[key: string]: {[key: string]: Artifact}};
  extraEnv?: {[key: string]: string};
  overrides?: Array<OverrideObject>;
  snapshots?: {[key: string]: {[key: string]: SnapshotInfo}};
}
