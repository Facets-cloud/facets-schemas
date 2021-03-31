import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserManagementComponent } from './user-management.component';
import {LayoutsModule} from "../../layouts/layouts.module";
import {NbButtonModule, NbCardModule, NbInputModule, NbSelectModule, NbUserModule} from "@nebular/theme";
import {Ng2SmartTableModule} from "ng2-smart-table";
import {UserRendererComponent} from "./user-renderer.component";
import {FormsModule} from "@angular/forms";



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
    FormsModule
  ]
})
export class UserManagementModule { }
