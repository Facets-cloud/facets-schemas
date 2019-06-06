import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { DeploymentstatusPage } from './deploymentstatus.page';

const routes: Routes = [
  {
    path: '',
    component: DeploymentstatusPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [DeploymentstatusPage]
})
export class DeploymentstatusPageModule {}
