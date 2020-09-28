/* tslint:disable */
import { EnvironmentConfiguration } from './environment-configuration';
import { EnvironmentMetaData } from './environment-meta-data';
export interface Environment {
  environmentConfiguration?: EnvironmentConfiguration;
  environmentMetaData?: EnvironmentMetaData;
  id?: string;
  name?: string;
}
