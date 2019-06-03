import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { AppdetailsPage } from './appdetails.page';
import { BuildPage } from '../build/build.page';
import { BuildPageModule } from '../build/build.module';

const routes: Routes = [
  {
    path: '',
    component: AppdetailsPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [AppdetailsPage],
})
export class AppdetailsPageModule {}
