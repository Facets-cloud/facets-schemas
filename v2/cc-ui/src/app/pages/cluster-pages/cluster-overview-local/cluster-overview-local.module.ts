import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterOverviewLocalComponent } from './cluster-overview-local.component';
import {LayoutsModule} from "../../../layouts/layouts.module";
import {NbButtonModule, NbCardModule, NbIconModule, NbSpinnerModule, NbUserModule} from "@nebular/theme";



@NgModule({
  declarations: [
    ClusterOverviewLocalComponent
  ],
  exports: [
    ClusterOverviewLocalComponent
  ],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    NbSpinnerModule,
    NbUserModule,
    NbButtonModule,
    NbIconModule
  ]
})
export class ClusterOverviewLocalModule { }
