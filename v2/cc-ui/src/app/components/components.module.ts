import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  NbActionsModule,
  NbAutocompleteModule,
  NbButtonModule,
  NbCardModule,
  NbContextMenuModule,
  NbIconModule,
  NbInputModule,
  NbLayoutModule,
  NbMenuModule,
  NbSelectModule,
  NbTooltipModule,
  NbUserModule
} from '@nebular/theme';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import {HeaderComponent} from './header/header.component';
import {RouterModule} from '@angular/router';
import {ResourceSelectorComponent} from './resource-selector/resource-selector.component';
import {FormsModule} from "@angular/forms";
import {ResourceTypeSelectorComponent} from './resource-type-selector/resource-type-selector.component';
import {CustomActionsComponent} from './custom-actions/custom-actions.component';
import {FormFieldComponent} from './form-field/form-field.component';
import {DirectivesModule} from "../directives/directives.module";
import {SafeHtmlPipePipe} from './safe-html-pipe.pipe';
import {SidebarComponent} from './sidebar/sidebar.component';
import {SmartTableRouterlink} from "./smart-table-routerlink/smart-table-routerlink.component";



@NgModule({
  declarations: [HeaderComponent, ResourceSelectorComponent, ResourceTypeSelectorComponent, CustomActionsComponent, FormFieldComponent, SafeHtmlPipePipe, SidebarComponent, SmartTableRouterlink],
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
    NbInputModule,
    NbIconModule,
    NbEvaIconsModule,
    NbContextMenuModule,
    NbTooltipModule,
    DirectivesModule,
    NbMenuModule
  ],
  providers: [],
  exports: [
    HeaderComponent,
    ResourceSelectorComponent,
    ResourceTypeSelectorComponent,
    CustomActionsComponent,
    FormFieldComponent,
    SafeHtmlPipePipe,
    SidebarComponent,
    SmartTableRouterlink
  ]
})
export class ComponentsModule {
}

