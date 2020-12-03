import {Component, OnInit, ViewChild} from '@angular/core';
import {
  UiDeploymentControllerService,
  UiCommonClusterControllerService,
  UiStackControllerService
} from 'src/app/cc-api/services';
import {ActivatedRoute, Router} from '@angular/router';
import {DeploymentLog} from 'src/app/cc-api/models';
import {NbDialogService, NbToastrService, NbSelectModule} from '@nebular/theme';
import {ApplicationControllerService} from '../../../cc-api/services/application-controller.service';
import {SimpleOauth2User} from '../../../cc-api/models/simple-oauth-2user';
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-cluster-releases',
  templateUrl: './cluster-releases.component.html',
  styleUrls: ['./cluster-releases.component.scss'],
})

export class ClusterReleasesComponent implements OnInit {

  clusterId = '';
  deployments: DeploymentLog[];
  loading = true;
  downStreamClusters = [];
  currentSignedOffDeployment: DeploymentLog;
  payload: any = '{}';
  user: SimpleOauth2User;
  releaseTypes = ['Hotfix'];
  releaseTypeSelection: any = 'Hotfix';
  applicationName: any = '';
  isUserAdmin: any;

  appName: any;
  stackName: any;

  constructor(private deploymentController: UiDeploymentControllerService,
              private u: UiStackControllerService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService,
              private dialogService: NbDialogService,
              private deploymentService: UiDeploymentControllerService,
              private applicationController: ApplicationControllerService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        this.clusterId = p.clusterId;
        this.deploymentController.getDeploymentsUsingGET1(this.clusterId).subscribe(
          t => {
            this.currentSignedOffDeployment = t.currentSignedOffDeployment;
            t.deployments.forEach(deployment =>
              deployment['allowSignoff'] = deployment.status === 'SUCCEEDED' &&
                deployment.stackVersion?.length > 0 &&
                deployment.tfVersion?.length > 0 &&
                !deployment.signedOff &&
                deployment.createdOn > this.currentSignedOffDeployment?.createdOn);
            t.deployments.forEach(
              x => {
                if (x['allowSignoff']) {
                  if (t.stack.vcs === 'GITHUB') {
                    x['compareUrl'] = t.stack.vcsUrl.replace('.git', '') + '/compare/'
                      + t.currentSignedOffDeployment?.stackVersion + '...' + x.stackVersion;
                  } else if (t.stack.vcs === 'BITBUCKET') {
                    x['compareUrl'] = t.stack.vcsUrl.replace('.git', '') + '/compare/' +
                      x.stackVersion + '%0' + t.currentSignedOffDeployment?.stackVersion;
                  }
                } else {
                  x['compareUrl'] = null;
                }
              }
            );
            this.deployments = t.deployments;
            this.downStreamClusters = t.downStreamClusterNames;
          },
          () => this.loading = false
        );
      }
      this.stackName = p.stackName;
    });
    this.applicationController.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
        this.isUserAdmin = (this.user.authorities.map(x => x.authority).includes('ROLE_ADMIN'))
          || this.user.authorities.map(x => x.authority).includes('ROLE_USER_ADMIN');
      }
    );

  }


  showDetails(dialog, deploymentId) {
    this.deploymentController.getDeploymentUsingGET({deploymentId: deploymentId, clusterId: this.clusterId}).subscribe(
      d => this.dialogService.open(dialog, {
        context: {
          changes: d.changesApplied,
          appDeployments: d.appDeployments,
          errors: d.errorLogs,
          overrideBuildSteps: d.overrideBuildSteps
        }
      }),
    );
  }

  confirmSignoff(signoff, deployment) {
    this.dialogService.open(signoff, {context: deployment}).onClose.subscribe(
      d => {
        this.loading = true;
        this.deploymentController.signOffDeploymentUsingPUT({deploymentId: d.id, clusterId: this.clusterId})
          .subscribe(
            x => {
              d.signedOff = x.signedOff;
              this.ngOnInit();
            },
          );
      },
    );
  }

  openTab(compareUrl) {
    window.open(compareUrl, "_blank");
  }

  openDeploymentPopup(deploymentUI) {
    this.dialogService.open(deploymentUI, {context: 'NA'}).onClose.subscribe(
      result => {
        if (result) {
          this.loading = true;
          console.log(this.releaseTypeSelection);
          console.log(this.applicationName);
          const applicationNameArray = this.applicationName.split(',');
          let targetsForOverride = '';
          for (let i = 0; i < applicationNameArray.length; i++) {
            applicationNameArray[i] = applicationNameArray[i].replace(/^\s*/, '').replace(/\s*$/, '');
            targetsForOverride = targetsForOverride
              .concat(' -target \'module.application.helm_release.application[\"' + applicationNameArray[i] + '\"]\'');
          }
          if (this.releaseTypeSelection === 'Hotfix') {
            this.payload = {
              releaseType: 'RELEASE',
              overrideBuildSteps: ['terraform apply ' + targetsForOverride + ' -auto-approve']
            };
          } else if (this.releaseTypeSelection === 'Release') {
            this.payload = {
              releaseType: 'RELEASE'
            };
          }
          console.log(this.payload);
          try {
            this.deploymentService.createDeploymentUsingPOST1({
              clusterId: this.clusterId,
              deploymentRequest: this.payload
            }).subscribe(c => {
                console.log(c);
                this.toastrService.success('Triggered terraform apply', 'Success');
                this.ngOnInit();
              },
              err => {
                this.toastrService.warning(err.error.message, 'Error');
              }
            );
          } catch (err) {
            console.log(err);
            console.log('Trigger failed');
            this.toastrService.warning('Trigger Failed', 'Error');
          }
        }
      },
    );
  }

  appSelected($event: string) {
    this.applicationName = $event;
  }
}
