/* tslint:disable */
export interface File {
  absolute?: boolean;
  absoluteFile?: File;
  absolutePath?: string;
  canonicalFile?: File;
  canonicalPath?: string;
  directory?: boolean;
  file?: boolean;
  freeSpace?: number;
  hidden?: boolean;
  name?: string;
  parent?: string;
  parentFile?: File;
  path?: string;
  totalSpace?: number;
  usableSpace?: number;
}
