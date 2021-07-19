import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationCenterComponent } from './notification-center.component';
import {LayoutsModule} from "../../layouts/layouts.module";
import {NbButtonModule, NbCardModule} from "@nebular/theme";
import {Ng2SmartTableModule} from "ng2-smart-table";
import {ComponentsModule} from "../../components/components.module";
import {FormsModule} from "@angular/forms";



@NgModule({
  declarations: [NotificationCenterComponent],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    ComponentsModule,
    FormsModule,
    NbButtonModule
  ]
})
export class NotificationCenterModule { }
