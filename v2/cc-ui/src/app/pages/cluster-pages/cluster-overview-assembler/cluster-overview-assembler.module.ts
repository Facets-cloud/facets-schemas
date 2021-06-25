import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterOverviewAssemblerComponent } from './cluster-overview-assembler.component';
import {ClusterOverviewModule} from "../cluster-overview/cluster-overview.module";
import {ClusterCreateLocalModule} from "../cluster-create-local/cluster-create-local.module";
import {ClusterOverviewLocalModule} from "../cluster-overview-local/cluster-overview-local.module";
import { AzureClusterCreateModule } from '../cluster-create-azure/cluster-create-azure.module';
import { ClusterOverviewAzureModule } from '../cluster-overview-azure/cluster-overview-azure.module';



@NgModule({
  declarations: [
    ClusterOverviewAssemblerComponent
  ],
  imports: [
    CommonModule,
    ClusterOverviewModule,
    ClusterCreateLocalModule,
    ClusterOverviewLocalModule,
    AzureClusterCreateModule,
    ClusterOverviewAzureModule,
  ]
})
export class ClusterOverviewAssemblerModule { }
