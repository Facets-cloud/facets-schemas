import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterOverviewComponent } from './cluster-overview.component';
import {NbButtonModule, NbCardModule, NbLayoutModule, NbSidebarModule} from "@nebular/theme";
import {RouterModule} from "@angular/router";
import {ComponentsModule} from "../../components/components.module";



@NgModule({
  declarations: [ClusterOverviewComponent],
  imports: [
    CommonModule,
    RouterModule, // RouterModule.forRoot(routes, { useHash: true }), if this is your app.module
    NbLayoutModule,
    NbSidebarModule, // NbSidebarModule.forRoot(), //if this is your app.module
    NbCardModule,
    ComponentsModule
  ]
})
export class ClusterOverviewModule { }
