import { Component, OnInit } from '@angular/core';
import { SnapshotInfo } from 'src/app/cc-api/models';
import { UiCommonClusterControllerService } from 'src/app/cc-api/services';
import { ActivatedRoute } from '@angular/router';
import { NbToastrService, NbDialogService } from '@nebular/theme';
import { PinSnapshotDialogComponent } from './pin-snapshot-dialog/pin-snapshot-dialog.component';
import { CreateSnapshotDialogComponent } from './create-snapshot-dialog/create-snapshot-dialog.component';

@Component({
  selector: 'app-cluster-disaster-recovery',
  templateUrl: './cluster-disaster-recovery.component.html',
  styleUrls: ['./cluster-disaster-recovery.component.scss']
})
export class ClusterDisasterRecoveryComponent implements OnInit {

  settings = {
    columns: {
      name: {
        title: 'Snapshot ID',
        editable: false
      },
      startTime: {
        title: 'Timestamp',
        valuePrepareFunction: function(startTime: string) {
          return new Date(startTime).toString();
        },
        editable: false
      },
      pinned: {
        title: 'Pinned',
        valuePrepareFunction: function(pinned: boolean) {
          return pinned ? 'YES' : 'NO';
        }
      }
    },
    noDataMessage: '',
    pager: {
      display: true,
      perPage: 5,
    },
    add: false,
    actions: {
      edit: false,
      delete: false,
      add: false,
      position: 'right',
      custom: [{ name: 'Pin', title: '<i class="eva-pin-outline eva"></i>', type: 'html' }]
    },
    hideSubHeader: true,
  };

  clusterId = '';
  
  resourceType = '';
  instanceName = '';
  
  createSnapshotResourceType = '';
  createSnapshotInstanceName = '';

  drData: Array<SnapshotInfo> = [];

  fetchSnapshotsSpinner = false;
  pinSnapshotSpinner = false;
  createSnapshotSpinner = false;

  constructor(
    private uiCommonClusterController: UiCommonClusterControllerService,
    private route: ActivatedRoute,
    private toastrService: NbToastrService,
    private dialogService: NbDialogService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        this.clusterId = p.clusterId;
      }
    });
  }

  pinSnapshot(event) {
    console.log(event);
    if (event.action === 'Pin') {
      if (event.data['pinned']) {
        this.toastrService.danger('Already Pinned', 'Error');
        return;
      }
      
      this.dialogService.open(PinSnapshotDialogComponent).onClose.subscribe(proceed => {
        if (proceed) {
          this.pinSnapshotSpinner = true;
          this.uiCommonClusterController.pinSnapshotUsingPOST1({
            snapshotInfo: event.data,
            resourceType: this.resourceType,
            instanceName: this.instanceName,
            clusterId: this.clusterId
          }).subscribe(snap => {
            console.log('pinned snapshot');
            this.fetchSnapshots();
            this.pinSnapshotSpinner = false;
          });  
        }
      });
    }
  }

  private async getPinnedSnapshot() {
    try {
      let pinnedSnapshot = await this.uiCommonClusterController.getPinnedSnapshotUsingGET1({
        resourceType: this.resourceType,
        instanceName: this.instanceName,
        clusterId: this.clusterId
      }).toPromise();

      return pinnedSnapshot;
    } catch (err) {
      console.log(err);
    }

    return null;
  }

  private filterPinnedSnapshot(pinnedSnapshot: SnapshotInfo, snapshots: Array<SnapshotInfo>) {
    if (!pinnedSnapshot) {
      return null;
    }

    let pinnedSnapshotPos = -1;
    for (let idx = 0; idx < snapshots.length; idx++) {
      const element = snapshots[idx];
      if (element.cloudSpecificId === pinnedSnapshot.cloudSpecificId) {
        pinnedSnapshotPos = idx;
        break;
      }
    }

    return pinnedSnapshotPos;
  }

  async fetchSnapshots() {
    console.log("fetching snapshots");
    this.fetchSnapshotsSpinner = true;
    try {
      let snapshots = await this.uiCommonClusterController.listSnapshotsUsingGET1({
        resourceType: this.resourceType,
        instanceName: this.instanceName,
        clusterId: this.clusterId
      }).toPromise();

      let pinnedSnapshot = await this.getPinnedSnapshot();
      let pinnedSnapshotPos = this.filterPinnedSnapshot(pinnedSnapshot, snapshots);

      if (pinnedSnapshotPos === -1) {
        snapshots.unshift(pinnedSnapshot);
      } else if (pinnedSnapshot != null && pinnedSnapshotPos >= 0) {
        snapshots.splice(pinnedSnapshotPos, 1);
        snapshots.unshift(pinnedSnapshot);
      }

      if (snapshots.length == 0) {
        this.toastrService.danger('No Snapshots Found', 'Error');
      } else {
        this.toastrService.success('', 'Success');
      }

      this.drData = snapshots;
      this.fetchSnapshotsSpinner = false;
    } catch (err) {
      console.log(err);
      this.fetchSnapshotsSpinner = false;
    }
  }

  createSnapshots() {
    console.log("creating snapshots");
    this.dialogService.open(CreateSnapshotDialogComponent).onClose.subscribe(proceed => {
      if (proceed) {
        this.createSnapshotSpinner = true;
        try {
          this.uiCommonClusterController.createSnapshotUsingPOST({
            resourceType: this.createSnapshotResourceType,
            instanceName: this.createSnapshotInstanceName,
            clusterId: this.clusterId
          }).subscribe(result => {
            if (!result) {
              this.toastrService.danger('Error: Could not create snapshot', 'Error');
            } else {
              this.toastrService.success('Snapshot creation triggered', 'Success');
            }
            this.fetchSnapshots();
            this.createSnapshotSpinner = false;
          });  
        } catch (err) {
          console.log(err);
          this.toastrService.danger('Error: Could not create snapshot', 'Error');
        }
      }
    });
  }

}
