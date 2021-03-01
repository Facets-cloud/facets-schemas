import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import {RouterModule} from '@angular/router';
import {
  NbActionsModule, NbButtonModule, NbCardModule,NbIconModule, NbLayoutModule, NbSelectModule, NbSidebarModule, NbSidebarService,
  NbUserModule
} from '@nebular/theme';
import {NbEvaIconsModule} from "@nebular/eva-icons";
import {ComponentsModule} from '../../components/components.module';
import {LayoutsModule} from '../../layouts/layouts.module';
import {Ng2SmartTableModule} from 'ng2-smart-table';



@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    RouterModule,
    NbCardModule,
    NbEvaIconsModule,
    NbIconModule,
    LayoutsModule,
    Ng2SmartTableModule,
    NbButtonModule
  ]
})
export class HomeModule { }
