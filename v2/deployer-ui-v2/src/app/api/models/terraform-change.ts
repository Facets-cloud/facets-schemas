/* tslint:disable */
export interface TerraformChange {
  resourceKey?: string;
  resourcePath?: string;
  type?: 'Destruction' | 'Creation' | 'Modifications';
}
