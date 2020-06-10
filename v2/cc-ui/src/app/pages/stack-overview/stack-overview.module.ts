import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StackOverviewComponent} from './stack-overview.component';
import {NbCardModule} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {LayoutsModule} from '../../layouts/layouts.module';


@NgModule({
  declarations: [StackOverviewComponent],
  imports: [
    CommonModule,
    RouterModule,
    LayoutsModule,
    NbCardModule,
  ]
})
export class StackOverviewModule {
}
