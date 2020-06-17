/* tslint:disable */
export interface PVC {
  accessMode?: 'ReadWriteOnce' | 'ReadOnlyMany' | 'ReadWriteMany';
  mountPath?: string;
  name?: string;
  storageSize?: number;
  volumeDirectory?: string;
}
