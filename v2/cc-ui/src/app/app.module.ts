import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NbThemeModule, NbLayoutModule} from '@nebular/theme';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import {HomeModule} from './pages/home/home.module';
import {AppComponent} from './app.component';
import {ClusterOverviewModule} from './pages/cluster-overview/cluster-overview.module';
import {ClusterReleasesModule} from './pages/cluster-releases/cluster-releases.module';
import {HttpClientModule} from '@angular/common/http';
import {GlobalErrorHandler} from './error-handler';
import {StackOverviewModule} from './pages/stack-overview/stack-overview.module';
import {NbAuthModule, NbPasswordAuthStrategy} from '@nebular/auth';
import {AuthGuard} from './auth-guard.service';

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
    HttpClientModule,
    StackOverviewModule
  ],
  providers: [
    {provide: ErrorHandler, useClass: GlobalErrorHandler},
    AuthGuard,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
