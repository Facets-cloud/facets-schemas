import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {Stack} from '../../cc-api/models/stack';
import {AbstractCluster} from '../../cc-api/models/abstract-cluster';
import {NbDialogService, NbToastrService} from '@nebular/theme';
import {PauseReleaseDialogComponent} from '../stack-overview/pause-release-dialog/pause-release-dialog.component';

@Component({
  selector: 'app-stack-overview',
  templateUrl: './stack-overview.component.html',
  styleUrls: ['./stack-overview.component.scss']
})
export class StackOverviewComponent implements OnInit {
  stack: Stack;
  tableData: any[];
  pauseReleases: boolean;

  clusterSettings = {
    columns: {
      id: {
        title: 'ClusterId',
      },
      name: {
        title: 'Cluster Name',
      },
      cloud: {
        title: 'Cloud Provider',
      },
      releaseStream: {
        title: 'Release Stream',
      },
      stackName: {
        title: 'Stack Name',
      },
      tz: {
        title: 'Time Zone',
      }
    },
    actions: {
      edit: false,
      delete: false,
      add: false,
      position: 'right',
      custom: [{name: 'View', title: '<i class="eva-eye-outline eva"></i>', type: 'html'}]
    },
    hideSubHeader: true,
  };

  constructor(private route: ActivatedRoute, private uiStackControllerService: UiStackControllerService, private router: Router, private dialogService: NbDialogService, private toastrService: NbToastrService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (!p.stackName) {
        // redirect to homepage
      }
      this.uiStackControllerService.getStackUsingGET(p.stackName).subscribe(
        s => {
          this.stack = s;
          this.pauseReleases = s.pauseReleases;
        }
      );
      this.uiStackControllerService.getClustersUsingGET1(p.stackName).subscribe(
        c => {
          this.tableData = c;
        }
      );
    });
  }

  gotoPage(x): void {
    if (x.action === 'View') {
      const clusterId = x.data.id;
      console.log('Navigate to ' + clusterId);
      this.router.navigate(['/capc/', x.data.stackName, 'cluster', clusterId]);
    }
  }

  createCluster(): void {
    this.router.navigate(['/capc/', this.stack.name , 'clusterCreate']);
  }

  newToggleClick(pauseReleases): void {
    const status: string = pauseReleases ? 'Enabled' : 'Disabled';
    this.dialogService.open(PauseReleaseDialogComponent, {context: {status: pauseReleases ? 'enable': 'disable'}}).onClose.subscribe(proceed => {
      console.log("received value from pause dialog " + proceed);
      if(proceed){
        this.uiStackControllerService.toggleReleaseUsingPOST1({toggleRelease: {stackName: this.stack.name, pauseReleases: !pauseReleases}, stackName: this.stack.name}).subscribe(
          s => {
            console.log("Prod Release %s for the stack %s", status, this.stack.name);
            this.toastrService.success(status.concat(' Prod Release'), 'Success');
          }
        );
      } else{
        this.pauseReleases = pauseReleases;
      }
    });
  }
}
