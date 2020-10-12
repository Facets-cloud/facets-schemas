import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterReleasesComponent } from './cluster-releases.component';
import {NbCardModule, NbLayoutModule, NbSidebarModule, NbSidebarService, NbIconComponent, NbIconModule, NbButtonModule, NbSpinnerModule, NbDialogModule, NbDialogService, NbTabsetModule, NbTooltipModule, NbListModule} from '@nebular/theme';
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
    NbSidebarModule,
    NbIconModule,
    NbButtonModule,
    NbSpinnerModule,
    NbDialogModule.forRoot(),
    NbTabsetModule,
    NbTooltipModule,
    NbListModule,
  ],
  providers: [NbSidebarService, NbDialogService]
})
export class ClusterReleasesModule { }
