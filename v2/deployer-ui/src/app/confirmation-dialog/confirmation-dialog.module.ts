import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ConfirmationDialogPage } from './confirmation-dialog.page';

const routes: Routes = [
  {
    path: '',
    component: ConfirmationDialogPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [ConfirmationDialogPage]
})
export class ConfirmationDialogPageModule {}
