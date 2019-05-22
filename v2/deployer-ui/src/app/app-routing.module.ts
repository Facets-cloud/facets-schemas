import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', loadChildren: './home/home.module#HomePageModule' },
  { path: 'signin', loadChildren: './login/login.module#LoginPageModule' },
  { path: ':applicationFamily/applications', loadChildren: './applications/applications.module#ApplicationsPageModule' },
  { path: ':applicationFamily/applications/:applicationId', loadChildren: './appdetails/appdetails.module#AppdetailsPageModule' },
  { path: ':applicationFamily/createapp', loadChildren: './createapp/createapp.module#CreateappPageModule' },
  { path: ':applicationFamily/applications/:applicationId/build', loadChildren: './build/build.module#BuildPageModule' },
  { path: ':applicationFamily/applications/:applicationId/builds/:buildId',
    loadChildren: './buildstatus/buildstatus.module#BuildstatusPageModule' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
