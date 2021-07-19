import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OauthClientsComponent } from './oauth-clients.component';
import {LayoutsModule} from "../../layouts/layouts.module";
import {NbButtonModule, NbCardModule} from "@nebular/theme";
import {Ng2SmartTableModule} from "ng2-smart-table";
import {DirectivesModule} from "../../directives/directives.module";
import {FormsModule} from "@angular/forms";
import {ComponentsModule} from "../../components/components.module";



@NgModule({
  declarations: [
    OauthClientsComponent
  ],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    DirectivesModule,
    NbButtonModule,
    FormsModule,
    ComponentsModule
  ]
})
export class OauthClientsModule { }
