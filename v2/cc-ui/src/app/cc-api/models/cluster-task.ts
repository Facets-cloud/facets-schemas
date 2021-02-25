/* tslint:disable */
export interface ClusterTask {
  clusterId?: string;
  id?: string;
  stackName?: string;
  taskStatus?: 'QUEUED' | 'EXECUTED' | 'DISABLED';
  tasks?: Array<string>;
}
