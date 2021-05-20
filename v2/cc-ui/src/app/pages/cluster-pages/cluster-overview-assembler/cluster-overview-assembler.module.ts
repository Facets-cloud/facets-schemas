import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterOverviewAssemblerComponent } from './cluster-overview-assembler.component';
import {ClusterOverviewModule} from "../cluster-overview/cluster-overview.module";
import {ClusterCreateLocalModule} from "../cluster-create-local/cluster-create-local.module";
import {ClusterOverviewLocalModule} from "../cluster-overview-local/cluster-overview-local.module";



@NgModule({
  declarations: [
    ClusterOverviewAssemblerComponent
  ],
  imports: [
    CommonModule,
    ClusterOverviewModule,
    ClusterCreateLocalModule,
    ClusterOverviewLocalModule
  ]
})
export class ClusterOverviewAssemblerModule { }
