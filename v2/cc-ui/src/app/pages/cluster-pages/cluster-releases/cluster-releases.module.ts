import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterReleasesComponent } from './cluster-releases.component';
import {NbCardModule, NbLayoutModule, NbSidebarModule, NbSidebarService} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {ComponentsModule} from '../../../components/components.module';



@NgModule({
  declarations: [ClusterReleasesComponent],
  imports: [
    CommonModule,
    RouterModule, // RouterModule.forRoot(routes, { useHash: true }), if this is your app.module
    NbLayoutModule,
    NbSidebarModule, // NbSidebarModule.forRoot(), //if this is your app.module
    NbCardModule,
    ComponentsModule,
    NbSidebarModule
  ],
  providers: [NbSidebarService]
})
export class ClusterReleasesModule { }
