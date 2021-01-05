import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NbThemeModule, NbLayoutModule, NbMenuModule, NbDialogService} from '@nebular/theme';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import {HomeModule} from './pages/home/home.module';
import {AppComponent} from './app.component';

import {HttpClientModule} from '@angular/common/http';
import {GlobalErrorHandler} from './error-handler';
import {StackOverviewModule} from './pages/stack-overview/stack-overview.module';
import {NbAuthModule, NbPasswordAuthStrategy} from '@nebular/auth';
import {AuthGuard} from './auth-guard.service';
import {ClusterPagesModule} from './pages/cluster-pages/cluster-pages.module';
import {ClusterOverridesModule} from './pages/cluster-pages/cluster-overrides/cluster-overrides.module';
import {ClusterReleasesModule} from './pages/cluster-pages/cluster-releases/cluster-releases.module';
import {ClusterCreateModule} from './pages/cluster-pages/cluster-create/cluster-create.module';
import {ClusterOverviewModule} from './pages/cluster-pages/cluster-overview/cluster-overview.module';
import { ClusterDisasterRecoveryModule } from './pages/cluster-pages/cluster-disaster-recovery/cluster-disaster-recovery.module';
import {ClusterAlertsModule} from "./pages/cluster-pages/cluster-alerts/cluster-alerts.module";


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    NbAuthModule.forRoot({
      strategies: [
        NbPasswordAuthStrategy.setup({
          name: 'email',
        }),
      ],
      forms: {},
    }),
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    NbThemeModule.forRoot({name: 'default'}),
    NbLayoutModule,
    NbEvaIconsModule,
    HomeModule,
    ClusterOverviewModule,
    ClusterReleasesModule,
    ClusterOverridesModule,
    ClusterDisasterRecoveryModule,
    ClusterPagesModule,
    ClusterCreateModule,
    ClusterAlertsModule,
    HttpClientModule,
    StackOverviewModule,
    NbMenuModule.forRoot(),
  ],
  providers: [
    {provide: ErrorHandler, useClass: GlobalErrorHandler},
    AuthGuard,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
