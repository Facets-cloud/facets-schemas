/* tslint:disable */
import { Artifact } from './artifact';
import { OverrideObject } from './override-object';
import { SnapshotInfo } from './snapshot-info';
export interface DeploymentContext {
  artifacts?: {[key: string]: {[key: string]: Artifact}};
  overrides?: Array<OverrideObject>;
  snapshots?: {[key: string]: {[key: string]: SnapshotInfo}};
}
