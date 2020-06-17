/* tslint:disable */
import { GrantedAuthority } from './granted-authority';
export interface SimpleOauth2User {
  attributes?: {};
  authorities?: Array<GrantedAuthority>;
  name?: string;
}
