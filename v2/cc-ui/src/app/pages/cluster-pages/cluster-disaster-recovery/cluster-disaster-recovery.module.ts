import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NbCardModule, NbLayoutModule, NbSidebarModule, NbSidebarService, NbIconComponent, NbIconModule, NbButtonModule, NbSpinnerModule, NbDialogModule, NbDialogService, NbTabsetModule, NbInputModule, NbToastrService, NbAccordionModule} from '@nebular/theme';
import {RouterModule} from '@angular/router';
import {ComponentsModule} from '../../../components/components.module';
import { ClusterDisasterRecoveryComponent } from './cluster-disaster-recovery.component';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { FormsModule } from '@angular/forms';
import { PinSnapshotDialogComponent } from './pin-snapshot-dialog/pin-snapshot-dialog.component';
import { CreateSnapshotDialogComponent } from './create-snapshot-dialog/create-snapshot-dialog.component';



@NgModule({
  declarations: [ClusterDisasterRecoveryComponent, PinSnapshotDialogComponent, CreateSnapshotDialogComponent],
  imports: [
    CommonModule,
    RouterModule, // RouterModule.forRoot(routes, { useHash: true }), if this is your app.module
    NbLayoutModule,
    NbSidebarModule, // NbSidebarModule.forRoot(), //if this is your app.module
    NbCardModule,
    ComponentsModule,
    NbSidebarModule,
    NbIconModule,
    NbInputModule,
    NbButtonModule,
    NbSpinnerModule,
    Ng2SmartTableModule,
    NbDialogModule.forRoot(),
    NbTabsetModule,
    FormsModule,
    RouterModule,
    NbAccordionModule
  ],
  providers: [NbSidebarService, NbDialogService, NbToastrService]
})
export class ClusterDisasterRecoveryModule { }
