import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterResourceDetailsComponent } from './cluster-resource-details.component';
import {LayoutsModule} from '../../../layouts/layouts.module';
import { NbCardModule, NbTabsetModule } from '@nebular/theme';
import { Ng2SmartTableModule } from 'ng2-smart-table';


@NgModule({
  declarations: [ClusterResourceDetailsComponent],
  imports: [
    CommonModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    NbTabsetModule
  ]
})
export class ClusterResourceDetailsModule { }
