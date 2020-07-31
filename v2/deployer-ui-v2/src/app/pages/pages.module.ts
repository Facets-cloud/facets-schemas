import { NgModule } from '@angular/core';
import { NbMenuModule, NbSearchModule, NbInputModule, NbAccordionModule, NbIconModule, NbButtonModule, NbActionsModule, NbCardModule, NbDialogModule, NbCheckboxComponent, NbCheckboxModule, NbTooltipComponent, NbTooltipModule, NbListModule, NbSelectModule, NbStepperModule, NbSpinnerModule, NbAlertModule, NbTabsetModule, NbToastrModule, NbToggleModule, NbCalendarModule, NbPopoverModule } from '@nebular/theme';
import { ThemeModule } from '../@theme/theme.module';
import { PagesComponent } from './pages.component';
import { DashboardModule } from './dashboard/dashboard.module';
import { PagesRoutingModule } from './pages-routing.module';
import { ApplicationsMenuComponent } from './applications-menu/applications-menu.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ApplicationPageComponent } from './application-page/application-page.component';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { AppInfoComponent } from './application-page/app-info/app-info.component';
import { BuildsListComponent } from './application-page/builds-list/builds-list.component';
import { BuildDialogComponent } from './application-page/build-dialog/build-dialog.component';
import { BuildPromoteComponent } from './application-page/build-promote/build-promote.component';
import { BuildInfoComponent } from './application-page/build-info/build-info.component';
import { SmartTableCustomActionsComponent } from './application-page/smart-table-custom-actions/smart-table-custom-actions.component';
import { BuildLogsComponent } from './application-page/build-logs/build-logs.component';
import { DeploymentPageComponent } from './deployment-page/deployment-page.component';
import { DeploymentStatusComponent, PodActionsColumnComponent } from './deployment-status/deployment-status.component';
import { CredentialManagementComponent } from './application-page/credential-management/credential-management.component';
import { SecretStatusColumnComponent } from './application-page/secret-status-column/secret-status-column.component';
import { RequestCredentialDialogComponent } from './application-page/request-credential-dialog/request-credential-dialog.component';
import { SetCredentialValueDialogComponent } from './application-page/set-credential-value-dialog/set-credential-value-dialog.component';
import { CreateApplicationPageComponent } from './create-application-page/create-application-page.component';
import { NumberComponentDynamicComponent } from './create-application-page/number-component-dynamic/number-component-dynamic.component';
import { CurrentDeploymentsComponent, ActionsColumn, DeploymentDetailsDialog, CurrentStatusColumn } from './application-page/current-deployments/current-deployments.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { BuildDetailsPageComponent } from './build-details-page/build-details-page.component';
import { AppDumpsComponent } from './application-page/app-dumps/app-dumps.component';
import { ButtonDownloadComponent } from './application-page/app-dumps/button.download.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { MonitoringComponent, NewRelicLinkViewComponent, MonitoringEnableButtonComponent } from './application-page/monitoring/monitoring.component';
import { ExcecutePodActionDialogComponent } from './deployment-status/excecute-pod-action-dialog/excecute-pod-action-dialog.component';
import { ExecutedActionsComponent, DialogActionInfoComponent, ButtonActionInfoComponent } from './application-page/executed-actions/executed-actions.component';
import { AlertingComponent, AlertingEnableButtonComponent, NewRelicAlertLinkViewComponent } from './application-page/alerting/alerting.component';
import { TestBuildDetailsComponent } from './application-page/test-build-details/test-build-details.component';
@NgModule({
  imports: [
    PagesRoutingModule,
    ThemeModule,
    NbMenuModule,
    DashboardModule,
    NbSearchModule,
    NbInputModule,
    FormsModule,
    NbAccordionModule,
    NbIconModule,
    NbButtonModule,
    NbActionsModule,
    Ng2SmartTableModule,
    NbCardModule,
    NbDialogModule,
    NbCheckboxModule,
    NbTooltipModule,
    NbListModule,
    NbSelectModule,
    NbStepperModule,
    NbSpinnerModule,
    NbAlertModule,
    NbTabsetModule,
    NbToastrModule,
    NbToggleModule,
    NbCalendarModule,
    NgSelectModule,
    NbPopoverModule,
  ],
  declarations: [
    PagesComponent,
    ApplicationsMenuComponent,
    ApplicationPageComponent,
    AppInfoComponent,
    BuildsListComponent,
    BuildDialogComponent,
    BuildPromoteComponent,
    BuildInfoComponent,
    SmartTableCustomActionsComponent,
    BuildLogsComponent,
    DeploymentPageComponent,
    DeploymentStatusComponent,
    PodActionsColumnComponent,
    CredentialManagementComponent,
    SecretStatusColumnComponent,
    RequestCredentialDialogComponent,
    SetCredentialValueDialogComponent,
    CreateApplicationPageComponent,
    NumberComponentDynamicComponent,
    CurrentDeploymentsComponent,
    ActionsColumn,
    DeploymentDetailsDialog,
    CurrentStatusColumn,
    UserManagementComponent,
    BuildDetailsPageComponent,
    AppDumpsComponent,
    ButtonDownloadComponent,
    MonitoringComponent,
    NewRelicLinkViewComponent,
    AlertingEnableButtonComponent,
    NewRelicAlertLinkViewComponent,
    MonitoringEnableButtonComponent,
    ExcecutePodActionDialogComponent,
    ExecutedActionsComponent,
    DialogActionInfoComponent,
    ButtonActionInfoComponent,
    AlertingComponent,
    TestBuildDetailsComponent,
  ],
  entryComponents: [BuildDialogComponent, ButtonDownloadComponent, PodActionsColumnComponent,
    ExcecutePodActionDialogComponent, DialogActionInfoComponent, ButtonActionInfoComponent],
})
export class PagesModule {
}
