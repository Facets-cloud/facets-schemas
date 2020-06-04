import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NbThemeModule, NbLayoutModule } from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import {HomeModule} from "./pages/home/home.module";
import {AppComponent} from "./app.component";
import {ClusterOverviewModule} from "./pages/cluster-overview/cluster-overview.module";
import {ClusterReleasesModule} from "./pages/cluster-releases/cluster-releases.module";

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
    ClusterReleasesModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
