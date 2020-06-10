import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {ClusterOverviewComponent} from './pages/cluster-overview/cluster-overview.component';
import {ClusterReleasesComponent} from './pages/cluster-releases/cluster-releases.component';
import {
  NbAuthComponent, NbLoginComponent, NbLogoutComponent
} from '@nebular/auth';


const routes: Routes = [
  {
    path: 'capc/cluster/:clusterId', component: ClusterOverviewComponent,
    children: [
      {
        path: 'releases',
        component: ClusterReleasesComponent,
      }
    ]
  },
  {path: 'capc/home', component: HomeComponent},
  {path: 'capc', redirectTo: '/capc/home', pathMatch: 'full'},
  {path: '', redirectTo: '/capc/home', pathMatch: 'full'},
  {path: 'capc/**', redirectTo: '/capc/home'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {


}
