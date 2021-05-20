import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserManagementComponent } from './user-management.component';
import {LayoutsModule} from "../../layouts/layouts.module";
import {NbButtonModule, NbCardModule, NbIconModule, NbInputModule, NbSelectModule, NbUserModule} from "@nebular/theme";
import {Ng2SmartTableModule} from "ng2-smart-table";
import {UserRendererComponent} from "./user-renderer.component";
import {FormsModule} from "@angular/forms";
import {ComponentsModule} from "../../components/components.module";
import {DirectivesModule} from "../../directives/directives.module";



@NgModule({
  declarations: [UserManagementComponent, UserRendererComponent],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    NbUserModule,
    NbInputModule,
    NbButtonModule,
    NbSelectModule,
    FormsModule,
    NbIconModule,
    ComponentsModule,
    DirectivesModule
  ]
})
export class UserManagementModule { }
