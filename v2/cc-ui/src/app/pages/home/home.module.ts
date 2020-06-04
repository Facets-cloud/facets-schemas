import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import {RouterModule} from "@angular/router";
import {
  NbActionsModule, NbButtonModule, NbCardModule, NbLayoutModule, NbSelectModule, NbSidebarModule, NbSidebarService,
  NbUserModule
} from "@nebular/theme";
import {ComponentsModule} from "../../components/components.module";



@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    RouterModule, // RouterModule.forRoot(routes, { useHash: true }), if this is your app.module
    NbLayoutModule,
    NbSidebarModule, // NbSidebarModule.forRoot(), //if this is your app.module
    NbButtonModule,
    NbUserModule,
    NbCardModule,
    ComponentsModule
  ],
  providers: [NbSidebarService],
})
export class HomeModule { }
