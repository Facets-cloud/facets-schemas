import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ClusterOverviewComponent} from './cluster-overview.component';
import {NbButtonModule, NbCardModule, NbLayoutModule, NbSidebarModule} from '@nebular/theme';
import {ActivatedRoute, RouterModule} from '@angular/router';
import {ComponentsModule} from '../../components/components.module';
import {AwsClusterControllerService} from '../../cc-api/services/aws-cluster-controller.service';
import {HttpClientModule} from "@angular/common/http";


@NgModule({
  declarations: [ClusterOverviewComponent],
  providers: [
    AwsClusterControllerService,
  ],
  imports: [
    CommonModule,
    RouterModule, // RouterModule.forRoot(routes, { useHash: true }), if this is your app.module
    NbLayoutModule,
    NbSidebarModule, // NbSidebarModule.forRoot(), //if this is your app.module
    NbCardModule,
    ComponentsModule,
    HttpClientModule
  ]
})
export class ClusterOverviewModule {
}
