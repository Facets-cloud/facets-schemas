import { Component, OnInit } from '@angular/core';
import { UiDeploymentControllerService, UiCommonClusterControllerService, UiStackControllerService } from 'src/app/cc-api/services';
import { ActivatedRoute, Router } from '@angular/router';
import { DeploymentLog } from 'src/app/cc-api/models';
import { NbDialogService } from '@nebular/theme';

@Component({
  selector: 'app-cluster-releases',
  templateUrl: './cluster-releases.component.html',
  styleUrls: ['./cluster-releases.component.scss']
})
export class ClusterReleasesComponent implements OnInit {

  clusterId = '';
  deployments: DeploymentLog[];
  loading = true;
  downStreamClusters = [];
  currentSignedOffDeployment: DeploymentLog;

  constructor(private deploymentController: UiDeploymentControllerService,
    private u: UiStackControllerService,
    private route: ActivatedRoute,
    private router: Router,
    private dialogService: NbDialogService) {
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
    });
  }

  showDetails(dialog, deploymentId) {
    this.deploymentController.getDeploymentUsingGET({ deploymentId: deploymentId, clusterId: this.clusterId }).subscribe(
      d => this.dialogService.open(dialog, {
        context: {
          changes: d.changesApplied,
          appDeployments: d.appDeployments
        }
      }),
    );
  }

  confirmSignoff(signoff, deployment) {
    this.dialogService.open(signoff, { context: deployment }).onClose.subscribe(
      d => {
        this.loading = true;
        this.deploymentController.signOffDeploymentUsingPUT({ deploymentId: d.id, clusterId: this.clusterId })
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

}
