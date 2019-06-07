import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { DeploymentsListPage } from './deployments-list.page';

const routes: Routes = [
  {
    path: '',
    component: DeploymentsListPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [DeploymentsListPage]
})
export class DeploymentsListPageModule {}
