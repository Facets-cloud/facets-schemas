import { Component, OnInit } from '@angular/core';
import { UiDeploymentControllerService } from 'src/app/cc-api/services';
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

  constructor(private deploymentController: UiDeploymentControllerService,
              private route: ActivatedRoute,
              private router: Router,
              private dialogService: NbDialogService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        this.clusterId = p.clusterId;
        this.deploymentController.getDeploymentsUsingGET1(this.clusterId).subscribe(
          t => this.deployments = t,
          () => this.loading = false,
        );
      }
    });
  }

  showDetails(dialog, deploymentId) {
    this.deploymentController.getDeploymentUsingGET({deploymentId: deploymentId, clusterId: this.clusterId}).subscribe(
      d => this.dialogService.open(dialog, { context: {
        statefulset_builds: d.buildSummary["builds"]["statefulset"]["success"],
        application_builds: d.buildSummary["builds"]["application"]["success"],
        cronjob_builds: d.buildSummary["builds"]["cronjob"]["success"],
        bootstrap_job_builds: d.buildSummary["builds"]["bootstrap_job"]["success"],
        serverless_builds: d.buildSummary["builds"]["serverless"]["success"],
        qasuite_builds: d.buildSummary["builds"]["qasuites"]["success"],
      } }),
    );
  }

}
