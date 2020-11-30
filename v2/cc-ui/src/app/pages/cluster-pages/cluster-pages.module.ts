import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterPagesComponent } from './cluster-pages.component';
import {RouterModule} from '@angular/router';
import {NbLayoutModule, NbInputModule, NbDialogService, NbToastrModule,
  NbDialogModule, NbIconModule, NbButtonModule, NbStepperModule} from '@nebular/theme';
import {NbToggleModule} from '@nebular/theme';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { ClusterCreateComponent } from './cluster-create/cluster-create.component';

@NgModule({
  declarations: [ClusterPagesComponent],
  imports: [
    CommonModule,
    RouterModule,
    NbLayoutModule,
    NbInputModule,
    NbIconModule,
    NbButtonModule,
    NbEvaIconsModule,
    NbToastrModule.forRoot(),
    NbDialogModule.forRoot(),
    NbLayoutModule,
    NbToggleModule,
    NbStepperModule
  ],
})
export class ClusterPagesModule { }
