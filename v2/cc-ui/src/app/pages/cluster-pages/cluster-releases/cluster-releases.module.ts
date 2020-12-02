import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterReleasesComponent } from './cluster-releases.component';
import {
  NbCardModule, NbLayoutModule, NbSidebarModule, NbSidebarService, NbIconComponent,
  NbIconModule, NbButtonModule, NbSpinnerModule, NbDialogModule, NbDialogService,
  NbTabsetModule, NbTooltipModule, NbListModule, NbSelectModule, NbInputModule, NbAutocompleteModule
} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {ComponentsModule} from '../../../components/components.module';
import { FormsModule } from '@angular/forms';
import {LayoutsModule} from '../../../layouts/layouts.module';




@NgModule({
  declarations: [ClusterReleasesComponent],
  imports: [
    LayoutsModule,
    CommonModule,
    RouterModule, // RouterModule.forRoot(routes, { useHash: true }), if this is your app.module
    NbLayoutModule,
    NbSidebarModule, // NbSidebarModule.forRoot(), //if this is your app.module
    NbCardModule,
    NbAutocompleteModule,
    ComponentsModule,
    NbSidebarModule,
    NbIconModule,
    NbButtonModule,
    NbSpinnerModule,
    NbDialogModule.forRoot(),
    NbTabsetModule,
    NbTooltipModule,
    NbListModule,
    NbSelectModule,
    FormsModule,
    NbInputModule,
  ],
  providers: [NbSidebarService, NbDialogService]
})
export class ClusterReleasesModule { }
