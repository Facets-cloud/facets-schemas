import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  NbButtonModule, NbCardModule, NbLayoutModule, NbListModule, NbMenuModule, NbMenuService,
  NbSidebarModule, NbToastrService
} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {ComponentsModule} from '../../../components/components.module';
import {ClusterOverridesComponent} from './cluster-overrides.component';
import {NgJsonEditorModule} from 'ang-jsoneditor';


@NgModule({
  declarations: [ClusterOverridesComponent],
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
    NbButtonModule
  ],
  providers: [
    NbMenuService,
    NbToastrService
  ]
})
export class ClusterOverridesModule {
}
