/* tslint:disable */
import { NgModule, ModuleWithProviders } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { ApiConfiguration, ApiConfigurationInterface } from './api-configuration';

import { WebMvcLinksHandlerService } from './services/web-mvc-links-handler.service';
import { OperationHandlerService } from './services/operation-handler.service';
import { ApplicationControllerService } from './services/application-controller.service';
import { CallbackControllerService } from './services/callback-controller.service';
import { UiArtifactoryControllerService } from './services/ui-artifactory-controller.service';
import { UiAwsClusterControllerService } from './services/ui-aws-cluster-controller.service';
import { MockCallBackControllerService } from './services/mock-call-back-controller.service';
import { UiCommonClusterControllerService } from './services/ui-common-cluster-controller.service';
import { UiDeploymentControllerService } from './services/ui-deployment-controller.service';
import { UiStackControllerService } from './services/ui-stack-controller.service';
import { UiTeamControllerService } from './services/ui-team-controller.service';
import { UiUserControllerService } from './services/ui-user-controller.service';
import { ArtifactoryControllerService } from './services/artifactory-controller.service';
import { ArtifactControllerService } from './services/artifact-controller.service';
import { AwsClusterControllerService } from './services/aws-cluster-controller.service';
import { BuildControllerService } from './services/build-controller.service';
import { CapillaryCloudCallbackControllerService } from './services/capillary-cloud-callback-controller.service';
import { CommonClusterControllerService } from './services/common-cluster-controller.service';
import { DeploymentControllerService } from './services/deployment-controller.service';
import { StackControllerService } from './services/stack-controller.service';
import { BasicErrorControllerService } from './services/basic-error-controller.service';

/**
 * Provider for all Api services, plus ApiConfiguration
 */
@NgModule({
  imports: [
    HttpClientModule
  ],
  exports: [
    HttpClientModule
  ],
  declarations: [],
  providers: [
    ApiConfiguration,
    WebMvcLinksHandlerService,
    OperationHandlerService,
    ApplicationControllerService,
    CallbackControllerService,
    UiArtifactoryControllerService,
    UiAwsClusterControllerService,
    MockCallBackControllerService,
    UiCommonClusterControllerService,
    UiDeploymentControllerService,
    UiStackControllerService,
    UiTeamControllerService,
    UiUserControllerService,
    ArtifactoryControllerService,
    ArtifactControllerService,
    AwsClusterControllerService,
    BuildControllerService,
    CapillaryCloudCallbackControllerService,
    CommonClusterControllerService,
    DeploymentControllerService,
    StackControllerService,
    BasicErrorControllerService
  ],
})
export class ApiModule {
  static forRoot(customParams: ApiConfigurationInterface): ModuleWithProviders<ApiModule> {
    return {
      ngModule: ApiModule,
      providers: [
        {
          provide: ApiConfiguration,
          useValue: {rootUrl: customParams.rootUrl}
        }
      ]
    }
  }
}
