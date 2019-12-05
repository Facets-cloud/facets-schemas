import { Component, OnInit, Input, TemplateRef } from '@angular/core';
import { DeploymentStatusDetails, Application, ApplicationPodDetails } from '../../api/models';
import { ActivatedRoute } from '@angular/router';
import { ApplicationControllerService } from '../../api/services';
import { NbDialogService, NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'deployment-status',
  templateUrl: './deployment-status.component.html',
  styleUrls: ['./deployment-status.component.scss'],
})
export class DeploymentStatusComponent implements OnInit {

  deploymentStatus: DeploymentStatusDetails;
  appPodDetails: Array<ApplicationPodDetails>;
  appFamily: any;
  applicationId: string;
  application: Application;
  environment: string;
  environmentVariables = [];
  stopping = false;
  starting = false;

  settings = {
    columns: {
      name: {
        title: 'Name',
      },
      creationTimestamp: {
        title: 'Created At',
      },
      podStatus: {
        title: 'Status',
      },
      ready: {
        title: 'Ready',
      },
      image: {
        title: 'Image',
      },
      restarts: {
        title: 'Restarts',
      },
      restartReason: {
        title: 'Last Restart Reason',
      },
    },
    actions: false,
    hideSubHeader: true,
  };

  envTableSettings = {
    columns: {
      key: {
        title: 'Name',
      },
      value: {
        title: 'Value',
      },
    },
    actions: false,
    hideSubHeader: true,
  };
  stopped: boolean;


  constructor(private activatedRoute: ActivatedRoute, private applicationControllerService: ApplicationControllerService,
    private dialogService: NbDialogService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      this.appFamily = params.get('appFamily');
      this.applicationId = params.get('applicationId');
      this.environment = params.get('environment');
      this.loadApplication();
      this.loadDeploymentStatus();
      this.loadPodDetails();
      this.loadApplication();
    });
  }

  loadApplication() {
    this.applicationControllerService.getApplicationUsingGET({
      applicationFamily: this.appFamily,
      applicationId: this.applicationId,
    }).subscribe(application => this.application = application);
  }

  loadDeploymentStatus() {
    this.applicationControllerService.getDeploymentStatusUsingGET({
      applicationFamily: this.appFamily,
      applicationId: this.applicationId,
      environment: this.environment,
    }).subscribe(deploymentStatus => {
      this.deploymentStatus = deploymentStatus;
      const envObj = deploymentStatus.deployment.environmentConfigs;
      this.environmentVariables = Object.keys(envObj).map(x => ({key: x, value: envObj[x]}));
      if (deploymentStatus.deployment.replicas.total === 0 && deploymentStatus.pods.length > 0) {
        this.stopping = true;
      } else {
        this.stopping = false;
      }
      if (deploymentStatus.deployment.replicas.total > 0 && deploymentStatus.pods.length === 0) {
        this.starting = true;
      } else {
        this.starting = false;
      }
      if (deploymentStatus.deployment.replicas.total === 0 && deploymentStatus.pods.length === 0) {
        this.stopped = true;
      }
    });
  }

  loadPodDetails() {
    this.applicationControllerService.getApplicationPodDetailsUsingGET({
      applicationFamily: this.appFamily,
      applicationId: this.applicationId,
      environment: this.environment,
    }).subscribe(podDetails => this.appPodDetails = podDetails);
  }

  haltPods() {
    this.applicationControllerService.haltApplicationUsingPOST({
      applicationFamily: this.application.applicationFamily,
      applicationId: this.application.id,
      environment: this.environment,
    }).subscribe(x => {
      this.loadDeploymentStatus();
    });
  }

  showHaltPodsDialog(dialog: TemplateRef<any>) {
    this.dialogService.open(dialog, {});
  }

  startPods() {
    this.applicationControllerService.resumeApplicationUsingPOST({
      applicationFamily: this.application.applicationFamily,
      applicationId: this.application.id,
      environment: this.environment,
    }).subscribe(x => {
      this.loadDeploymentStatus();
    });
  }
}
