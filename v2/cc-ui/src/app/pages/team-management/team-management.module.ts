import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamManagementComponent } from './team-management.component';
import { RouterModule } from '@angular/router';
import { NbAccordionModule, NbButtonModule, NbCardModule, NbDialogModule, NbSelectModule, NbTabsetModule } from '@nebular/theme';
import { LayoutsModule } from 'src/app/layouts/layouts.module';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { ComponentsModule } from 'src/app/components/components.module';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [TeamManagementComponent],
  imports: [
    CommonModule,
    RouterModule,
    NbCardModule,
    LayoutsModule,
    Ng2SmartTableModule,
    NbAccordionModule,
    NbTabsetModule,
    NbButtonModule,
    ComponentsModule,
    NbDialogModule,
    NbSelectModule,
    FormsModule,
  ]
})
export class TeamManagementModule { }
