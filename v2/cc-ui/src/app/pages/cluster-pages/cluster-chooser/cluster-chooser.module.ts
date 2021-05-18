import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterChooserComponent } from './cluster-chooser.component';
import {LayoutsModule} from "../../../layouts/layouts.module";
import {NbButtonModule, NbCardModule, NbUserModule} from "@nebular/theme";
import {DirectivesModule} from "../../../directives/directives.module";



@NgModule({
  declarations: [
    ClusterChooserComponent
  ],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    NbButtonModule,
    DirectivesModule,
    NbUserModule
  ]
})
export class ClusterChooserModule { }
