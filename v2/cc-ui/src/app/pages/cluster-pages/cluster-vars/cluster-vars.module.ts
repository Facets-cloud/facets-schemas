import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterVarsComponent } from './cluster-vars.component';
import {LayoutsModule} from "../../../layouts/layouts.module";
import {NbButtonModule, NbCardModule, NbListModule, NbSpinnerModule} from "@nebular/theme";
import {Ng2SmartTableModule} from "ng2-smart-table";



@NgModule({
  declarations: [ClusterVarsComponent],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    NbButtonModule,
    NbListModule,
    NbSpinnerModule
  ]
})
export class ClusterVarsModule { }
