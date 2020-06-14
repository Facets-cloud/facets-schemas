import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NbCardModule, NbLayoutModule, NbListModule, NbSidebarModule} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {ComponentsModule} from '../../../components/components.module';
import {ClusterOverridesComponent} from './cluster-overrides.component';


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
    NbListModule
  ]
})
export class ClusterOverridesModule {
}
