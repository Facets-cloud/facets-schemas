import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StackOverviewComponent} from './stack-overview.component';
import {NbButtonModule, NbCardModule, NbDialogModule, NbDialogService, NbIconModule, NbToastrService, NbToggleModule} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {LayoutsModule} from '../../layouts/layouts.module';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {NbEvaIconsModule} from "@nebular/eva-icons";
import { FormsModule } from '@angular/forms';
import { PauseReleaseDialogComponent } from './pause-release-dialog/pause-release-dialog.component';
import {DirectivesModule} from "../../directives/directives.module";


@NgModule({
  declarations: [StackOverviewComponent, PauseReleaseDialogComponent],
  imports: [
    CommonModule,
    RouterModule,
    LayoutsModule,
    NbCardModule,
    Ng2SmartTableModule,
    NbIconModule,
    NbEvaIconsModule,
    NbToggleModule,
    NbDialogModule,
    NbButtonModule,
    FormsModule,
    NbButtonModule,
    DirectivesModule
  ]
})
export class StackOverviewModule {
}
