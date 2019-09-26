import { NgModule } from '@angular/core';
import { NbCardModule, NbInputModule } from '@nebular/theme';

import { ThemeModule } from '../../@theme/theme.module';
import { DashboardComponent } from './dashboard.component';

@NgModule({
  imports: [
    NbCardModule,
    ThemeModule,
    NbInputModule,
  ],
  declarations: [
    DashboardComponent,
  ],
})
export class DashboardModule { }
