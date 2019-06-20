import { ErrorPage } from './../error/error.page';
import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Application, Deployment, Build, EnvironmentVariable } from '../api/models';
import { ApplicationControllerService } from '../api/services';
import { NavController, LoadingController, ModalController } from '@ionic/angular';
import { CssSelector } from '@angular/compiler';

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
  @Input() deployment: Deployment = {
    environment: "",
    configurations: [{}],
    horizontalPodAutoscaler: {}
  };

  constructor(private activatedRoute: ActivatedRoute, private applicationControllerService: ApplicationControllerService,
    private navController: NavController, private loadingController: LoadingController, private modalController:ModalController) { }

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
        this.applicationControllerService.getEnvironmentMetaDataUsingGET(applicationFamily).subscribe(e => this.environments = e.map(x=>x.name));
      }
    );
  }

  envSelected(environment: string) {
    this.applicationControllerService.getCurrentDeploymentUsingGET(
      {
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
        environment: environment
      }
    ).subscribe(
      deployment => {
        if(deployment) {
          this.deployment = deployment;
          this.deployment.id = null;
        } else {
          this.deployment = {
            environment: environment,
            configurations: [{}],
            horizontalPodAutoscaler: {}
          };
        }
      });
  }

  addConf() {
    this.deployment.configurations.push({});
  }

  deploy() {
    this.loadingController.create({
      message: 'Creating...',
      duration: 60000
    }).then(
      res => {
        res.present();
        this.deployment.buildId = this.buildId;
        this.applicationControllerService.deployUsingPOST({
          applicationFamily: this.application.applicationFamily,
          applicationId: this.application.id,
          environment: this.deployment.environment,
          deployment: this.deployment
        }).subscribe(
          (deployment: Deployment) => {
            res.remove();
            this.navController.navigateForward(`/${this.application.applicationFamily}/applications/${this.application.id}/deployments/${this.deployment.environment}`);
          },
          err => {
            res.remove();
            this.modalController.create(
              {
                component: ErrorPage,
                componentProps: {
                  errorMessage: 'Failed'
                }
              }
            ).then(x => x.present());
          }
        )
    });
  }

}
