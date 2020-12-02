import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  NbActionsModule,
  NbAutocompleteModule,
  NbButtonModule,
  NbCardModule,
  NbLayoutModule,
  NbSelectModule,
  NbSidebarModule,
  NbSidebarService,
  NbUserModule,
  NbInputModule,
} from '@nebular/theme';
import {HeaderComponent} from './header/header.component';
import {RouterModule} from '@angular/router';
import { ResourceSelectorComponent } from './resource-selector/resource-selector.component';
import {FormsModule} from "@angular/forms";



@NgModule({
  declarations: [HeaderComponent, ResourceSelectorComponent],
  imports: [
    CommonModule,
    NbLayoutModule,
    NbButtonModule,
    NbUserModule,
    NbActionsModule,
    NbCardModule,
    NbSelectModule,
    RouterModule,
    NbAutocompleteModule,
    FormsModule,
    NbInputModule
  ],
  providers: [],
  exports: [
    HeaderComponent,
    ResourceSelectorComponent,
  ]
})
export class ComponentsModule { }

