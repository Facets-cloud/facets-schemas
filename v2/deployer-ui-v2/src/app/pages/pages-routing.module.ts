import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PagesComponent } from './pages.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ApplicationPageComponent } from './application-page/application-page.component';
import { DeploymentPageComponent } from './deployment-page/deployment-page.component';
import { DeploymentStatusComponent } from './deployment-status/deployment-status.component';
import { CreateApplicationPageComponent } from './create-application-page/create-application-page.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { BuildDetailsPageComponent } from './build-details-page/build-details-page.component';
import {ApplicationMetricsAllComponent} from "./application-metrics-all/application-metrics-all.component";
import { SigninComponent } from './signin/signin.component';

const routes: Routes = [{
  path: '',
  component: PagesComponent,
  children: [
    {
      path: 'signin',
      component: SigninComponent
    },
    {
      path: 'dashboard',
      component: DashboardComponent,
    },
    {
      path: '',
      redirectTo: 'dashboard',
      pathMatch: 'full',
    },
    {
      path: 'applications/:appFamily/:applicationId',
      component: ApplicationPageComponent,
    },
    {
      path: 'applications/:appFamily/:applicationId/builds/:buildId',
      component: BuildDetailsPageComponent,
    },
    {
      path: 'applications/:appFamily/:applicationId/builds/:buildId/deploy',
      component: DeploymentPageComponent,
    },
    {
      path: 'applications/:appFamily/:applicationId/deploymentStatus/:environment',
      component: DeploymentStatusComponent,
    },
    {
      path: 'applications/create',
      component: CreateApplicationPageComponent,
    },
    {
      path: 'applications/:appFamily/:applicationId/edit',
      component: CreateApplicationPageComponent,
    },
    {
      path: 'users',
      component: UserManagementComponent,
    },
    {
      path: 'family/metrics',
      component: ApplicationMetricsAllComponent,
    },
  ],
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {
}
