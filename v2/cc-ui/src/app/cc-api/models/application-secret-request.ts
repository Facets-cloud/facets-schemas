/* tslint:disable */
export interface ApplicationSecretRequest {
  description?: string;
  secretName?: string;
  secretType?: 'ENVIRONMENT' | 'FILE';
}
