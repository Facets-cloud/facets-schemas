import { BrowserModule } from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NbThemeModule, NbLayoutModule } from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import {HomeModule} from './pages/home/home.module';
import {AppComponent} from './app.component';
import {ClusterOverviewModule} from './pages/cluster-overview/cluster-overview.module';
import {ClusterReleasesModule} from './pages/cluster-releases/cluster-releases.module';
import {HttpClientModule} from '@angular/common/http';
import {GlobalErrorHandler} from './error-handler';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    NbThemeModule.forRoot({ name: 'default' }),
    NbLayoutModule,
    NbEvaIconsModule,
    HomeModule,
    ClusterOverviewModule,
    ClusterReleasesModule,
    HttpClientModule,
  ],
  providers: [{provide: ErrorHandler, useClass: GlobalErrorHandler}],
  bootstrap: [AppComponent]
})
export class AppModule { }
