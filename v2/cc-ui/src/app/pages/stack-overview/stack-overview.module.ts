import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StackOverviewComponent} from './stack-overview.component';
import {NbButtonModule, NbCardModule, NbIconModule} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {LayoutsModule} from '../../layouts/layouts.module';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {NbEvaIconsModule} from "@nebular/eva-icons";


@NgModule({
  declarations: [StackOverviewComponent],
  imports: [
    CommonModule,
    RouterModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    NbIconModule,
    NbEvaIconsModule
  ]
})
export class StackOverviewModule {
}
