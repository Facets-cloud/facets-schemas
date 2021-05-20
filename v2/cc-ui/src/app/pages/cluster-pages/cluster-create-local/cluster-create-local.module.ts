import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterCreateLocalComponent } from './cluster-create-local.component';
import {LayoutsModule} from "../../../layouts/layouts.module";
import {NbAccordionModule, NbButtonModule, NbCardModule} from "@nebular/theme";
import {FormsModule} from "@angular/forms";
import {ComponentsModule} from "../../../components/components.module";



@NgModule({
  declarations: [
    ClusterCreateLocalComponent
  ],
  exports: [
    ClusterCreateLocalComponent
  ],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    FormsModule,
    ComponentsModule,
    NbAccordionModule,
    NbButtonModule
  ]
})
export class ClusterCreateLocalModule { }
