import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArtifactoryManagementComponent } from './artifactory-management.component';
import {LayoutsModule} from "../../layouts/layouts.module";
import {NbButtonModule, NbCardModule} from "@nebular/theme";
import {Ng2SmartTableModule} from "ng2-smart-table";
import {ComponentsModule} from "../../components/components.module";
import {FormsModule} from "@angular/forms";



@NgModule({
  declarations: [
    ArtifactoryManagementComponent
  ],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    NbButtonModule,
    ComponentsModule,
    FormsModule
  ]
})
export class ArtifactoryManagementModule { }
