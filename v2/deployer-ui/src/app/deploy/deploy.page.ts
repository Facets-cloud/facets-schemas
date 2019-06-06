import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Application, Deployment, Build } from '../api/models';
import { ApplicationControllerService } from '../api/services';

@Component({
  selector: 'app-deploy',
  templateUrl: './deploy.page.html',
  styleUrls: ['./deploy.page.scss'],
})
export class DeployPage implements OnInit {

  application: Application;
  build: Build;
  buildId: string;
  environments: string[];
  deployment: Deployment = {
    environment: "",
    configurations: []
  };

  constructor(private activatedRoute: ActivatedRoute, private applicationControllerService: ApplicationControllerService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        var applicationFamily:  'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS' = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        var applicationId: string = params.get("applicationId");
        this.buildId = params.get("buildId");
        this.applicationControllerService.getApplicationUsingGET({
          applicationFamily: applicationFamily,
          applicationId: applicationId
        }).subscribe(
          application => this.application = application
        );
        this.applicationControllerService.getBuildUsingGET({
          applicationFamily: applicationFamily,
          applicationId: applicationId,
          buildId: this.buildId
        }).subscribe(build => this.build = build);
        this.applicationControllerService.getEnvironmentsUsingGET(applicationFamily).subscribe(e => this.environments = e);
      }
    );
  }

  addConf() {
    this.deployment.configurations.push({});
  }

  deploy() {
    console.log(this.deployment);
    this.deployment.buildId = this.buildId;
    this.applicationControllerService.deployUsingPOST({
      applicationFamily: this.application.applicationFamily,
      applicationId: this.application.id,
      environment: this.deployment.environment,
      deployment: this.deployment
    }).subscribe(deployment => console.log(deployment));
  }

}
