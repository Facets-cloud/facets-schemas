import {StackCreateComponent} from './pages/cluster-pages/stack-create/stack-create.component';
import {ClusterCreateComponent} from './pages/cluster-pages/cluster-create/cluster-create.component';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {ClusterOverviewComponent} from './pages/cluster-pages/cluster-overview/cluster-overview.component';
import {ClusterReleasesComponent} from './pages/cluster-pages/cluster-releases/cluster-releases.component';
import {StackOverviewComponent} from './pages/stack-overview/stack-overview.component';
import {AuthGuard} from './auth-guard.service';
import {ClusterOverridesComponent} from './pages/cluster-pages/cluster-overrides/cluster-overrides.component';
import {ClusterPagesComponent} from './pages/cluster-pages/cluster-pages.component';
import {ClusterDisasterRecoveryComponent} from './pages/cluster-pages/cluster-disaster-recovery/cluster-disaster-recovery.component';
import {ClusterAlertsComponent} from "./pages/cluster-pages/cluster-alerts/cluster-alerts.component";
import {ClusterResourceDetailsComponent} from './pages/cluster-pages/cluster-resource-details/cluster-resource-details.component';
import {ClusterTunnelComponent} from "./pages/cluster-pages/cluster-tunnel/cluster-tunnel.component";
import {TeamsOverviewComponent} from './pages/teams-overview/teams-overview.component';
import {TeamManagementComponent} from './pages/team-management/team-management.component';
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {UserManagementComponent} from "./pages/user-management/user-management.component";
import {ArtifactoryManagementComponent} from "./pages/artifactory-management/artifactory-management.component";
import {ClusterChooserComponent} from "./pages/cluster-pages/cluster-chooser/cluster-chooser.component";
import {ClusterCreateLocalComponent} from "./pages/cluster-pages/cluster-create-local/cluster-create-local.component";
import {ClusterOverviewAssemblerComponent} from "./pages/cluster-pages/cluster-overview-assembler/cluster-overview-assembler.component";
import {AzureClusterCreateComponent} from "./pages/cluster-pages/cluster-create-azure/cluster-create-azure.component";


const routes: Routes = [
  {
    path: 'capc/login', component: LoginPageComponent
  },
  {
    path: 'capc/home', component: HomeComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/createStack', component: StackCreateComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/users', component: UserManagementComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/artifactories', component: ArtifactoryManagementComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/teams', component: TeamsOverviewComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/teams/:teamId', component: TeamManagementComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/stack/:stackName', component: StackOverviewComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/:stackName/clusterCreate/aws', component: ClusterCreateComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/:stackName/clusterCreate/azure', component: AzureClusterCreateComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/:stackName/clusterCreate/local', component: ClusterCreateLocalComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/:stackName/chooseClusterCreate', component: ClusterChooserComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/stack/:stackName/edit', component: StackCreateComponent, canActivate: [AuthGuard]
  },
  {
    path: 'capc/:stackName/cluster/:clusterId', component: ClusterPagesComponent, canActivate: [AuthGuard],
    children: [
      {
        path: 'overview',
        component: ClusterOverviewAssemblerComponent,
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
      {
        path: 'edit/aws',
        component: ClusterCreateComponent,
      },
      {
        path: 'edit/local',
        component: ClusterCreateLocalComponent,
      },
      {
        path: 'alerts',
        component: ClusterAlertsComponent,
      },
      {
        path: 'resource-details',
        component: ClusterResourceDetailsComponent,
      },
      {
        path: 'tools',
        component: ClusterTunnelComponent,
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
