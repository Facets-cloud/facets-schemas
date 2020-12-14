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
import { ResourceTypeSelectorComponent } from './resource-type-selector/resource-type-selector.component';



@NgModule({
  declarations: [HeaderComponent, ResourceSelectorComponent, ResourceTypeSelectorComponent],
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
    ResourceTypeSelectorComponent,
  ]
})
export class ComponentsModule { }

