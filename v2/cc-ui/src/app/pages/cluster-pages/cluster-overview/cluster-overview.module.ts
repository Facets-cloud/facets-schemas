import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ClusterOverviewComponent} from './cluster-overview.component';
import {
  NbButtonModule, NbCardModule, NbLayoutModule, NbSidebarModule, NbAccordionModule, NbToastrService,
  NbInputModule, NbSpinnerModule, NbIconModule, NbListModule, NbUserModule, NbToggleModule,
  NbSelectModule, NbActionsModule
} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {ComponentsModule} from '../../../components/components.module';
import {AwsClusterControllerService} from '../../../cc-api/services/aws-cluster-controller.service';
import {HttpClientModule} from '@angular/common/http';
import {LayoutsModule} from '../../../layouts/layouts.module';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { FormsModule } from '@angular/forms';
import {NbEvaIconsModule} from "@nebular/eva-icons";


@NgModule({
  declarations: [ClusterOverviewComponent],
  providers: [
    AwsClusterControllerService,
    NbToastrService,
  ],
  imports: [
    CommonModule,
    RouterModule, // RouterModule.forRoot(routes, { useHash: true }), if this is your app.module
    NbLayoutModule,
    NbSidebarModule, // NbSidebarModule.forRoot(), //if this is your app.module
    NbCardModule,
    ComponentsModule,
    HttpClientModule,
    LayoutsModule,
    NbAccordionModule,
    Ng2SmartTableModule,
    FormsModule,
    NbInputModule,
    NbButtonModule,
    NbSpinnerModule,
    NbIconModule,
    NbEvaIconsModule,
    NbListModule,
    NbUserModule,
    NbToggleModule,
    NbSelectModule,
    NbActionsModule
  ]
})
export class ClusterOverviewModule {
}
