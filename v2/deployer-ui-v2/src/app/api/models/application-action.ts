/* tslint:disable */
export interface ApplicationAction {
  actionType?: 'GENERIC' | 'CUSTOM';
  applicationId?: string;
  arguments?: string;
  argumentsRegex?: string;
  buildType?: 'MVN' | 'JAVA8_LIBRARY' | 'FREESTYLE_DOCKER' | 'DOTNET_CORE' | 'MVN_IONIC' | 'JDK6_MAVEN2' | 'MJ_NUGET' | 'DOTNET_CORE22' | 'DOTNET_CORE3' | 'SBT' | 'NPM';
  id?: string;
  metadata?: {};
  name?: string;
  path?: string;
}
