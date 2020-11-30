import { ClusterCreateComponent } from './pages/cluster-pages/cluster-create/cluster-create.component';
import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {ClusterOverviewComponent} from './pages/cluster-pages/cluster-overview/cluster-overview.component';
import {ClusterReleasesComponent} from './pages/cluster-pages/cluster-releases/cluster-releases.component';
import {
  NbAuthComponent, NbLoginComponent, NbLogoutComponent
} from '@nebular/auth';
import {StackOverviewComponent} from './pages/stack-overview/stack-overview.component';
import {AuthGuard} from './auth-guard.service';
import {ClusterOverridesComponent} from './pages/cluster-pages/cluster-overrides/cluster-overrides.component';
import {AppComponent} from './app.component';
import {ClusterPagesComponent} from './pages/cluster-pages/cluster-pages.component';
import { ClusterDisasterRecoveryComponent } from './pages/cluster-pages/cluster-disaster-recovery/cluster-disaster-recovery.component';


const routes: Routes = [
  {
    path: 'capc/home', component: HomeComponent, canActivate: [AuthGuard]
  },
  {path: 'capc/stack/:stackName', component: StackOverviewComponent, canActivate: [AuthGuard]},
  {path: 'capc/:stackName/clusterCreate', component: ClusterCreateComponent, canActivate: [AuthGuard]},
  {
    path: 'capc/:stackName/cluster/:clusterId', component: ClusterPagesComponent, canActivate: [AuthGuard],
    children: [
      {
        path: 'overview',
        component: ClusterOverviewComponent,
      },
      {
        path: 'releases',
        component: ClusterReleasesComponent,
      },
      {
        path: 'overrides',
        component: ClusterOverridesComponent,
      },
      {
        path: 'disaster-recovery',
        component: ClusterDisasterRecoveryComponent,
      },
      {path: '', redirectTo: 'overview', pathMatch: 'full'},
      {path: '**', redirectTo: 'overview', pathMatch: 'full'},
    ]
  },
  {path: 'capc', redirectTo: '/capc/home', pathMatch: 'full'},
  {path: '', redirectTo: '/capc/home', pathMatch: 'full'},
  {path: 'capc/**', redirectTo: '/capc/home'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
      paramsInheritanceStrategy: 'always',
      onSameUrlNavigation: "reload"
    }
  )],
  exports: [RouterModule]
})
export class AppRoutingModule {


}
