/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { CoreModule } from './@core/core.module';
import { ThemeModule } from './@theme/theme.module';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import {
  NbChatModule,
  NbDatepickerModule,
  NbDialogModule,
  NbMenuModule,
  NbSidebarModule,
  NbToastrModule,
  NbWindowModule,
} from '@nebular/theme';
import { FormsModule } from '@angular/forms';
import { BuildInfoComponent } from './pages/application-page/build-info/build-info.component';
import { BuildDialogComponent } from './pages/application-page/build-dialog/build-dialog.component';
import { PagesModule } from './pages/pages.module';
import { SmartTableCustomActionsComponent } from './pages/application-page/smart-table-custom-actions/smart-table-custom-actions.component';
import { BuildLogsComponent } from './pages/application-page/build-logs/build-logs.component';
import { SecretStatusColumnComponent } from './pages/application-page/secret-status-column/secret-status-column.component';
import { RequestCredentialDialogComponent } from './pages/application-page/request-credential-dialog/request-credential-dialog.component';
import { SetCredentialValueDialogComponent } from './pages/application-page/set-credential-value-dialog/set-credential-value-dialog.component';
import { NumberComponentDynamicComponent } from './pages/create-application-page/number-component-dynamic/number-component-dynamic.component';
import { ActionsColumn, DeploymentDetailsDialog, CurrentStatusColumn } from './pages/application-page/current-deployments/current-deployments.component';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ThemeModule.forRoot(),

    NbSidebarModule.forRoot(),
    NbMenuModule.forRoot(),
    NbDatepickerModule.forRoot(),
    NbDialogModule.forRoot(),
    NbWindowModule.forRoot(),
    NbToastrModule.forRoot(),
    NbChatModule.forRoot({
      messageGoogleMapKey: 'AIzaSyA_wNuCzia92MAmdLRzmqitRGvCF7wCZPY',
    }),
    CoreModule.forRoot(),
    PagesModule,
  ],
  bootstrap: [AppComponent],
  entryComponents: [BuildDialogComponent,
    BuildInfoComponent,
    SmartTableCustomActionsComponent,
    BuildLogsComponent,
    SecretStatusColumnComponent,
    RequestCredentialDialogComponent,
    SetCredentialValueDialogComponent,
    NumberComponentDynamicComponent,
    ActionsColumn,
    DeploymentDetailsDialog,
    CurrentStatusColumn,
  ],
})
export class AppModule {
}
