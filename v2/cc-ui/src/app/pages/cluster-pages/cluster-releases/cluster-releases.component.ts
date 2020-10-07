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
            var signedOffDeployment = t.filter(x => x.signedOff)[0];
            t.forEach(deployment =>
              deployment['allowSignoff'] = deployment.status === 'SUCCEEDED' &&
              deployment.stackVersion?.length > 0 &&
              deployment.tfVersion?.length > 0 &&
              !deployment.signedOff &&
              deployment.createdOn > signedOffDeployment.createdOn);

            this.deployments = t;
          },
          () => this.loading = false,
          () => this.u.getClustersUsingGET1(p.stackName).subscribe(clusters => {
            this.downStreamClusters =
              clusters.filter(x => x.cdPipelineParent === this.clusterId)
                .filter(x => x.requireSignOff)
                .map(x => x.name);
          })
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

}
