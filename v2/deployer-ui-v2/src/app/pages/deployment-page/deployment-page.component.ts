import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Build, Deployment, EnvironmentMetaData, Application, DeploymentStatusDetails } from '../../api/models';
import { ApplicationControllerService } from '../../api/services';
import { NbStepperComponent, NbStepComponent, NbToastrService } from '@nebular/theme';
import { MessageBus } from '../../@core/message-bus';

@Component({
  selector: 'deployment-page',
  templateUrl: './deployment-page.component.html',
  styleUrls: ['./deployment-page.component.scss'],
})
export class DeploymentPageComponent implements OnInit {

  loading: boolean = true;
  appFamily: any;
  applicationId: string;
  buildId: string;
  build: Build;
  deployment: Deployment = {
    configurations: [],
    horizontalPodAutoscaler: { minReplicas: 1, maxReplicas: 1, threshold: 50 },
  };
  environments: EnvironmentMetaData[];
  application: Application;
  deploymentStatus: DeploymentStatusDetails;

  settings = {
    columns: {
      name: {
        title: 'Key',
        filter: false,
        width: '45%',
      },
      value: {
        title: 'Value',
        filter: false,
        width: '45%',
      },
    },
    noDataMessage: '',
    add: {
      addButtonContent: '<i class="nb-plus"></i>',
      createButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmCreate: true,
    },
    delete: {
      deleteButtonContent: '<i class="nb-trash"></i>',
      confirmDelete: true,
    },
    edit: {
      editButtonContent: '<i class="nb-edit"></i>',
      saveButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
    },
    pager: {
      display: true,
      perPage: 5,
    },
    actions: {
      position: 'right',
    },
  };

  constructor(private activatedRoute: ActivatedRoute, private router: Router,
    private applicationControllerService: ApplicationControllerService,
    private nbToastrService: NbToastrService, private messageBus: MessageBus) { }

  async ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        this.appFamily = params.get('appFamily');
        this.applicationId = params.get('applicationId');
        this.buildId = params.get('buildId');
        this.loadBuild();
        this.loadApplication();
      },
    );
  }

  loadBuild() {
    this.applicationControllerService.getBuildUsingGET({
      applicationFamily: this.appFamily,
      applicationId: this.applicationId,
      buildId: this.buildId,
    }).subscribe(build => {
      this.build = build;
      this.loadEnvironments();
    });
  }

  loadEnvironments() {
    this.applicationControllerService.getEnvironmentMetaDataUsingGET(this.appFamily)
      .subscribe(async environments => {
        if (!this.build.promoted) {
          environments = environments.filter(x => x.environmentType === 'QA');
        }
        if (this.application.strictGitFlowModeEnabled) {
          const tags = await this.applicationControllerService.getApplicationTagsUsingGET({
            applicationFamily: this.application.applicationFamily,
            applicationId: this.application.id,
          }).toPromise();
        }
        this.environments = environments;
      });
  }

  loadApplication() {
    this.applicationControllerService.getApplicationUsingGET({
      applicationFamily: this.appFamily,
      applicationId: this.applicationId,
    }).subscribe(application => this.application = application);
  }

  async deploy(stepper: NbStepperComponent) {
    stepper.next();
    this.deployment.buildId = this.buildId;
    this.deployment.applicationFamily = this.appFamily;
    this.deployment.applicationId = this.applicationId;
    this.applicationControllerService.deployUsingPOST({
      environment: this.deployment.environment,
      deployment: this.deployment,
      applicationId: this.applicationId,
      applicationFamily: this.appFamily,
    }).subscribe(
      (deployment: Deployment) => {
        stepper.next();
        this.messageBus.deploymentTopic.next(true);
        new Promise(resolve => setTimeout(resolve, 1000)).then(x => this.router.navigate(
          ['pages', 'applications',
            this.appFamily, this.applicationId,
            'deploymentStatus', this.deployment.environment]));
      },
    );
  }

  environmentSelected(stepper: NbStepperComponent) {
    this.applicationControllerService.getCurrentDeploymentUsingGET(
      {
        environment: this.deployment.environment,
        applicationId: this.applicationId,
        applicationFamily: this.appFamily,
      },
    ).subscribe(
      deployment => {
        if (deployment) {
          this.deployment.podSize = deployment.podSize,
          this.deployment.configurations = deployment.configurations;
          this.deployment.horizontalPodAutoscaler = deployment.horizontalPodAutoscaler;
          this.deployment.schedule = deployment.schedule;
        }
        stepper.next();
      },
    );
  }

  scheduleEnteredStepper(stepper: any) {
    stepper.next();
  }

  autoScaleOrDeploy(stepper: NbStepperComponent) {

  }

  validateConfig(event) {

    if (this.deployment.configurations.map(x => x.name).includes(event.newData['name'])) {
      event.confirm.reject();
      this.nbToastrService.danger('Duplicate keys not allowed', 'Error');
    } else {
      event.confirm.resolve(event.newData);
    }
  }

  onDeleteConfirm(event) {
    console.log(event);
    for (let i = 0; i < this.deployment.configurations.length; i++) {
      if (this.deployment.configurations[i].name === event.data['name']) {
        this.deployment.configurations.splice(i, 1);
      }
    }
    event.confirm.resolve(event.data);
  }
}
