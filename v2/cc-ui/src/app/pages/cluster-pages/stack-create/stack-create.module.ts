import { StackCreateComponent } from './stack-create.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule} from '@angular/router';
import {LayoutsModule} from '../../../layouts/layouts.module';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import { FormsModule } from '@angular/forms';
import {NbButtonModule, NbCardModule, NbIconModule, NbStepperModule,
  NbSelectModule, NbToggleModule, NbInputModule} from '@nebular/theme';

@NgModule({
  declarations: [StackCreateComponent],
  imports: [
    CommonModule,
    NbStepperModule,
    NbCardModule,
    NbButtonModule,
    NbSelectModule,
    NbToggleModule,
    NbInputModule,
    NbIconModule,
    FormsModule,
    NbEvaIconsModule,
    Ng2SmartTableModule,
    LayoutsModule,
    RouterModule
  ]
})
export class StackCreateModule { }
