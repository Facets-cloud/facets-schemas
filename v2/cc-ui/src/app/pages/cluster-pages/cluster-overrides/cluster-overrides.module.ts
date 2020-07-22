import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  NbButtonModule, NbCardModule, NbLayoutModule, NbListModule, NbMenuModule, NbMenuService,
  NbSidebarModule, NbToastrService, NbInputModule, NbDialogService, NbIconModule, NbSpinnerModule, NbDialogRef
} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {ComponentsModule} from '../../../components/components.module';
import {ClusterOverridesComponent} from './cluster-overrides.component';
import {NgJsonEditorModule} from 'ang-jsoneditor';
import { FormsModule } from '@angular/forms';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { PopupAppOverrideComponent } from './popup-app-override/popup-app-override.component';


@NgModule({
  declarations: [ClusterOverridesComponent, PopupAppOverrideComponent],
  imports: [
    CommonModule,
    RouterModule, // RouterModule.forRoot(routes, { useHash: true }), if this is your app.module
    NbLayoutModule,
    NbSidebarModule, // NbSidebarModule.forRoot(), //if this is your app.module
    NbCardModule,
    ComponentsModule,
    NbSidebarModule,
    NbListModule,
    NbMenuModule,
    NgJsonEditorModule,
    NbButtonModule,
    FormsModule,
    Ng2SmartTableModule,
    NbIconModule,
    NbInputModule
  ],
  providers: [
    NbMenuService,
    NbToastrService,
    NbDialogService,
    PopupAppOverrideComponent
  ]
})
export class ClusterOverridesModule {
}
