import { CustomActionsComponent } from './../../components/custom-actions/custom-actions.component';
import { Application } from './../../cc-api/models/application';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {Stack} from '../../cc-api/models/stack';
import {AbstractCluster} from '../../cc-api/models/abstract-cluster';
import {ApplicationControllerService } from '../../cc-api/services/application-controller.service';
import {SimpleOauth2User} from '../../cc-api/models/simple-oauth-2user';
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
      },
      custom: {
        type: 'custom',
        renderComponent: CustomActionsComponent,
        title: 'Actions',
        width: '12%'
      }
    },
    actions: false,
    hideSubHeader: true
  };

  constructor(private route: ActivatedRoute,
              private uiStackControllerService: UiStackControllerService,
              private router: Router,
              private applicationController: ApplicationControllerService,
              private dialogService: NbDialogService,
              private toastrService: NbToastrService) {
  }
  user: SimpleOauth2User;
  isUserAdmin: any;

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
    this.applicationController.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
        this.isUserAdmin = (this.user.authorities.map(x => x.authority).includes('ROLE_ADMIN'))
        || this.user.authorities.map(x => x.authority).includes('ROLE_CC-ADMIN');
      }
    );
  }


  createCluster(): void {
    if (this.isUserAdmin){
    this.router.navigate(['/capc/', this.stack.name , 'chooseClusterCreate']);
    }
  }

  errorHandler(error) {
    this.toastrService.warning(error.error.message, 'Error');
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
          },
          error => {
            this.pauseReleases = pauseReleases;
            this.errorHandler(error);
          }
        );
      } else{
        this.pauseReleases = pauseReleases;
      }
    });
  }
}
