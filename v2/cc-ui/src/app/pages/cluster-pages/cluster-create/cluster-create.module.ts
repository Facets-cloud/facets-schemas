import { ClusterCreateComponent } from './cluster-create.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  NbButtonModule, NbCardModule, NbIconModule, NbStepperModule,
  NbSelectModule, NbToggleModule, NbInputModule, NbToastrService, NbAccordionModule
} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {LayoutsModule} from '../../../layouts/layouts.module';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import { FormsModule } from '@angular/forms';
import {ComponentsModule} from "../../../components/components.module";


@NgModule({
  declarations: [ClusterCreateComponent],
  providers: [
    NbToastrService,
  ],
  imports: [
    NbButtonModule,
    NbInputModule,
    CommonModule,
    RouterModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    NbIconModule,
    NbEvaIconsModule,
    NbStepperModule,
    FormsModule,
    NbSelectModule,
    NbToggleModule,
    ComponentsModule,
    NbAccordionModule
  ]
})
export class ClusterCreateModule { }
