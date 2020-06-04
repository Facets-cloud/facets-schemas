import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from "./pages/home/home.component";
import {ClusterOverviewComponent} from "./pages/cluster-overview/cluster-overview.component";
import {ClusterReleasesComponent} from "./pages/cluster-releases/cluster-releases.component";


const routes: Routes = [
  { path: 'cc/cluster/:clusterId/releases', component: ClusterReleasesComponent },
  { path: 'cc/cluster/:clusterId', component: ClusterOverviewComponent  },
  { path: 'cc/home', component: HomeComponent },
  { path: '', redirectTo: '/cc/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/cc/home' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {


}
