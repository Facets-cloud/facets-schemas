import { StackCreateComponent } from './stack-create.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule} from '@angular/router';
import {LayoutsModule} from '../../../layouts/layouts.module';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import { FormsModule } from '@angular/forms';
import {
  NbButtonModule, NbCardModule, NbIconModule,
  NbSelectModule, NbToggleModule, NbInputModule, NbTooltipModule
} from '@nebular/theme';
import {ComponentsModule} from "../../../components/components.module";
import {DirectivesModule} from "../../../directives/directives.module";

@NgModule({
  declarations: [StackCreateComponent],
  imports: [
    CommonModule,
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
    RouterModule,
    NbTooltipModule,
    ComponentsModule,
    DirectivesModule
  ]
})
export class StackCreateModule { }
