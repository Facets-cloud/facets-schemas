/* tslint:disable */
export interface Artifact {
  applicationName?: string;
  artifactUri?: string;
  artifactory?: string;
  buildDescription?: string;
  buildId?: string;
  id?: string;
  releaseStream?: 'QA' | 'STAGING' | 'PROD' | 'QA2';
  releaseType?: 'HOTFIX' | 'RELEASE';
}
