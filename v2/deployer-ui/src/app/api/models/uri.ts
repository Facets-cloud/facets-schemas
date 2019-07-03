/* tslint:disable */
export interface URI {
  rawAuthority?: string;
  absolute?: boolean;
  fragment?: string;
  host?: string;
  opaque?: boolean;
  path?: string;
  port?: number;
  query?: string;
  authority?: string;
  rawFragment?: string;
  rawPath?: string;
  rawQuery?: string;
  rawSchemeSpecificPart?: string;
  rawUserInfo?: string;
  scheme?: string;
  schemeSpecificPart?: string;
  userInfo?: string;
}
