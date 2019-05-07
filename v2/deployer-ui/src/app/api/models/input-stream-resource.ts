/* tslint:disable */
import { File } from './file';
import { InputStream } from './input-stream';
import { URI } from './uri';
import { URL } from './url';
export interface InputStreamResource {
  description?: string;
  file?: File;
  filename?: string;
  inputStream?: InputStream;
  open?: boolean;
  readable?: boolean;
  uri?: URI;
  url?: URL;
}
