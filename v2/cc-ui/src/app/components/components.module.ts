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
    NbIconModule, NbContextMenuModule
} from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import {HeaderComponent} from './header/header.component';
import {RouterModule} from '@angular/router';
import { ResourceSelectorComponent } from './resource-selector/resource-selector.component';
import {FormsModule} from "@angular/forms";
import { ResourceTypeSelectorComponent } from './resource-type-selector/resource-type-selector.component';
import { CustomActionsComponent } from './custom-actions/custom-actions.component';



@NgModule({
  declarations: [HeaderComponent, ResourceSelectorComponent, ResourceTypeSelectorComponent, CustomActionsComponent],
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
        NbContextMenuModule
    ],
  providers: [],
  exports: [
    HeaderComponent,
    ResourceSelectorComponent,
    ResourceTypeSelectorComponent,
    CustomActionsComponent,
  ]
})
export class ComponentsModule { }

