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

  deploymentFailed: boolean = false;
  deploymentError: {error: {message: "unknown error"}};
  loading: boolean = true;
  appFamily: any;
  applicationId: string;
  buildId: string;
  build: Build;
  deployment: Deployment = {
    configurations: [],
    horizontalPodAutoscaler: { minReplicas: 1, maxReplicas: 1, threshold: 50 },
    replicas: 1,
  };
  environments: EnvironmentMetaData[];
  application: Application;
  deploymentStatus: DeploymentStatusDetails;

  settings = {
    mode: 'inline',
    columns: {
      name: {
        title: 'Key',
        filter: false,
        width: '45%',
        editable: false,
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
      confirmSave: true,
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
      this.loadApplication();
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
    }).subscribe(application => {
      this.application = application;
      this.loadEnvironments();
    });
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
      err => {
        this.deploymentError = err;
        this.deploymentFailed = true;
        stepper.next();
        console.log(err);
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
      (deployment: Deployment) => {
        if (deployment) {
          this.deployment.podSize = deployment.podSize,
          this.deployment.configurations = deployment.configurations;
          this.deployment.horizontalPodAutoscaler = deployment.horizontalPodAutoscaler;
          this.deployment.schedule = deployment.schedule;
          this.deployment.replicas = deployment.replicas;
        }
        stepper.next();
      },
      err => {
        stepper.next();
      },
    );
  }

  scheduleEnteredStepper(stepper: any) {
    stepper.next();
  }

  autoScaleOrDeploy(stepper: NbStepperComponent) {

  }

  isValidEnvVariableName(name: string) {
    return /^[-._a-zA-Z0-9]+$/.test(name);
  }

  isValidEnvVariableValue(value: string) {
    return true;
  }

  isZKPublishTrue(name: string, value: string) {
    if (name === 'zkPublish') {
      if (value === 'true' ) {
        return true;
      }
    }
    return false;
  }

  isZKPublishFalse(name: string, value: string) {
    if (name === 'zkPublish') {
      if (value === 'false' ) {
        return true;
      }
    }
    return false;
  }

  showWarningToastforPublish(duration) {
    this.nbToastrService.warning(
      'This will switch to K8s-mode and publish service to Zookeeper',
      'Attention',
      { duration });
  }

  showWarningToastforUnpublish(duration) {
    this.nbToastrService.warning(
      'This will switch to Test-mode and unpublish service from Zookeeper',
      'Attention',
      { duration });
  }

  validateConfig(event) {
    const data = event.newData;

    if (!this.isValidEnvVariableName(data['name'])) {
      event.confirm.reject();
      this.nbToastrService.danger('Environment variable key should match regex ^[-._a-zA-Z0-9]+$', 'Error');
      return;
    }

    if (!this.isValidEnvVariableValue(data['value'])) {
      event.confirm.reject();
      this.nbToastrService.danger('Environment variable value should not contain whitespaces', 'Error');
      return;
    }

    if (this.deployment.configurations.map(x => x['name']).includes(data['name'])) {
      event.confirm.reject();
      this.nbToastrService.danger('Duplicate keys not allowed', 'Error');
    } else {
      event.confirm.resolve(event.newData);
    }

    if (this.isZKPublishTrue(data['name'], data['value'])) {
      this.showWarningToastforPublish(10000);
    }

    if (this.isZKPublishFalse(data['name'], data['value'])) {
        this.showWarningToastforUnpublish(10000);
    }
  }

  validateEditConfig(event) {
    const data = event.newData;

    if (!this.isValidEnvVariableName(data['name'])) {
      event.confirm.reject();
      this.nbToastrService.danger('Environment variable key should match regex ^[-._a-zA-Z0-9]+$', 'Error');
      return;
    }

    if (!this.isValidEnvVariableValue(data['value'])) {
      event.confirm.reject();
      this.nbToastrService.danger('Environment variable value should not contain whitespaces', 'Error');
      return;
    }

    if (this.isZKPublishTrue(data['name'], data['value'])) {
      this.showWarningToastforPublish(10000);
    }

    if (this.isZKPublishFalse(data['name'], data['value'])) {
        this.showWarningToastforUnpublish(10000);
    }
    event.confirm.resolve(event.newData);
  }

  onDeleteConfirm(event) {
    for (let i = 0; i < this.deployment.configurations.length; i++) {
      if (this.deployment.configurations[i]['name'] === event.data['name']) {
        this.deployment.configurations.splice(i, 1);
      }
    }
    event.confirm.resolve(event.data);
  }
}
