import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  NbActionsModule, NbButtonModule, NbCardModule, NbLayoutModule, NbSelectModule, NbSidebarModule, NbSidebarService,
  NbUserModule
} from "@nebular/theme";
import {HeaderComponent} from "./header/header.component";
import {RouterModule} from "@angular/router";



@NgModule({
  declarations: [HeaderComponent],
  imports: [
    CommonModule,
    NbLayoutModule,
    NbButtonModule,
    NbUserModule,
    NbActionsModule,
    NbCardModule,
    NbSelectModule,
    RouterModule
  ],
  providers: [],
  exports: [
    HeaderComponent,
  ]
})
export class ComponentsModule { }

