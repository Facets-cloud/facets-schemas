/* tslint:disable */
import { NgModule, ModuleWithProviders } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { ApiConfiguration, ApiConfigurationInterface } from './api-configuration';

import { ApplicationControllerService } from './services/application-controller.service';
import { CallbackControllerService } from './services/callback-controller.service';
import { UiAwsClusterControllerService } from './services/ui-aws-cluster-controller.service';
import { UiDeploymentControllerService } from './services/ui-deployment-controller.service';
import { UiCommonClusterControllerService } from './services/ui-common-cluster-controller.service';
import { UiStackControllerService } from './services/ui-stack-controller.service';
import { AwsClusterControllerService } from './services/aws-cluster-controller.service';
import { BuildControllerService } from './services/build-controller.service';
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
    ApplicationControllerService,
    CallbackControllerService,
    UiAwsClusterControllerService,
    UiDeploymentControllerService,
    UiCommonClusterControllerService,
    UiStackControllerService,
    AwsClusterControllerService,
    BuildControllerService,
    CommonClusterControllerService,
    DeploymentControllerService,
    StackControllerService,
    BasicErrorControllerService
  ],
})
export class ApiModule {
  static forRoot(customParams: ApiConfigurationInterface): ModuleWithProviders {
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
