import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamsOverviewComponent } from './teams-overview.component';
import { RouterModule } from '@angular/router';
import { NbCardModule } from '@nebular/theme';
import { LayoutsModule } from 'src/app/layouts/layouts.module';
import { Ng2SmartTableModule } from 'ng2-smart-table';



@NgModule({
  declarations: [TeamsOverviewComponent],
  imports: [
    CommonModule,
    RouterModule,
    NbCardModule,
    LayoutsModule,
    Ng2SmartTableModule
  ]
})
export class TeamsOverviewModule { }
