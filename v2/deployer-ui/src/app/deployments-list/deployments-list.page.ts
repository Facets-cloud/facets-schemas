import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApplicationControllerService } from '../api/services';
import { Deployment, Build } from '../api/models';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-deployments-list',
  templateUrl: './deployments-list.page.html',
  styleUrls: ['./deployments-list.page.scss'],
})
export class DeploymentsListPage implements OnInit {

  applicationId: string;
  applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  environments: string[];
  deployments: { [environment: string]: Deployment; } = {};
  builds: { [environment: string]: Build; } = {};
  constructor(private activatedRoute: ActivatedRoute, private applicationControllerService: ApplicationControllerService,
    private navController: NavController) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        this.applicationId = params.get("applicationId");
        this.applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        this.applicationControllerService.getEnvironmentsUsingGET(this.applicationFamily).subscribe(
          environments => {
            this.environments = environments;
            this.environments.forEach(e => this.getDeploymentSatus(e));
          }
        )
      });
  }

  getDeploymentSatus(environment: string) {
    this.applicationControllerService.getCurrentDeploymentUsingGET(
      {
        applicationFamily: this.applicationFamily,
        applicationId: this.applicationId,
        environment: environment
      }
    ).subscribe(
      d => {
        this.deployments[environment] = d;
        this.applicationControllerService.getBuildUsingGET(
          {
            applicationFamily: this.applicationFamily,
            applicationId: this.applicationId,
            buildId: d.buildId
          }
        ).subscribe(
          build => this.builds[environment] = build
        );
      }
    );
  }

  showDeploymentStatus(deployment: Deployment) {
    this.navController.navigateForward(`/${this.applicationFamily}/applications/${this.applicationId}/deployments/${deployment.environment}`);
  }

}
