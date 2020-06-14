import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterPagesComponent } from './cluster-pages.component';
import {RouterModule} from '@angular/router';
import {NbLayoutModule} from '@nebular/theme';



@NgModule({
  declarations: [ClusterPagesComponent],
  imports: [
    CommonModule,
    RouterModule,
    NbLayoutModule
  ]
})
export class ClusterPagesModule { }
