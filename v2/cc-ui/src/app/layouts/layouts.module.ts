import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NbLayoutModule} from '@nebular/theme';
import {ComponentsModule} from '../components/components.module';
import {LayoutOneColumnComponent} from './layout-one-column/layout-one-column.component';


@NgModule({
  declarations: [LayoutOneColumnComponent],
  imports: [
    CommonModule,
    NbLayoutModule,
    ComponentsModule
  ],
  exports: [
    LayoutOneColumnComponent
  ]
})
export class LayoutsModule {
}
