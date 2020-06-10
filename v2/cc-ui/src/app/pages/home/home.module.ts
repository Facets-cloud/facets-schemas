import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import {RouterModule} from '@angular/router';
import {
  NbActionsModule, NbButtonModule, NbCardModule, NbLayoutModule, NbSelectModule, NbSidebarModule, NbSidebarService,
  NbUserModule
} from '@nebular/theme';
import {ComponentsModule} from '../../components/components.module';
import {LayoutsModule} from '../../layouts/layouts.module';



@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    RouterModule,
    NbCardModule,
    LayoutsModule
  ]
})
export class HomeModule { }
