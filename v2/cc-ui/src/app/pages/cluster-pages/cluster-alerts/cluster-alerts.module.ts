import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterAlertsComponent } from './cluster-alerts.component';
import {LayoutsModule} from "../../../layouts/layouts.module";
import {
  NbAccordionModule,
  NbBadgeModule, NbButtonModule,
  NbCardModule,
  NbIconModule,
  NbListModule,
  NbSpinnerModule,
  NbUserModule,
  NbInputModule
} from "@nebular/theme";
import {RouterModule} from "@angular/router";



@NgModule({
  declarations: [ClusterAlertsComponent],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    NbAccordionModule,
    NbBadgeModule,
    RouterModule,
    NbIconModule,
    NbListModule,
    NbUserModule,
    NbButtonModule,
    NbSpinnerModule,
    NbInputModule
  ]
})
export class ClusterAlertsModule { }
