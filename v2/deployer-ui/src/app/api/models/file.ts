/* tslint:disable */
export interface File {
  freeSpace?: number;
  absolute?: boolean;
  absolutePath?: string;
  canonicalFile?: File;
  canonicalPath?: string;
  directory?: boolean;
  file?: boolean;
  absoluteFile?: File;
  hidden?: boolean;
  name?: string;
  parent?: string;
  parentFile?: File;
  path?: string;
  totalSpace?: number;
  usableSpace?: number;
}
