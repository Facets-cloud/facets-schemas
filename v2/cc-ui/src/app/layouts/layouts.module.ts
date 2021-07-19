import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NbIconModule, NbLayoutModule, NbSidebarModule} from '@nebular/theme';
import {ComponentsModule} from '../components/components.module';
import {LayoutOneColumnComponent} from './layout-one-column/layout-one-column.component';
import { LayoutOneColumnSidebarComponent } from './layout-one-column-sidebar/layout-one-column-sidebar.component';


@NgModule({
  declarations: [LayoutOneColumnComponent, LayoutOneColumnSidebarComponent],
  imports: [
    CommonModule,
    NbLayoutModule,
    ComponentsModule,
    NbSidebarModule,
    NbIconModule
  ],
  exports: [
    LayoutOneColumnComponent,
    LayoutOneColumnSidebarComponent
  ]
})
export class LayoutsModule {
}
