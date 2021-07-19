/* tslint:disable */
export interface CustomOAuth2ClientRegistration {
  clientId?: string;
  clientSecret?: string;
  isSystemConfigured?: boolean;
  loginButtonText?: string;
  provider?: 'GOOGLE' | 'GITHUB' | 'FACEBOOK' | 'OKTA';
  registrationId?: string;
  scope?: string;
  systemConfigured?: boolean;
}
