import { Component, OnInit, Input } from '@angular/core';
import { DeploymentStatusDetails } from '../../api/models';
import { ActivatedRoute } from '@angular/router';
import { ApplicationControllerService } from '../../api/services';

@Component({
  selector: 'deployment-status',
  templateUrl: './deployment-status.component.html',
  styleUrls: ['./deployment-status.component.scss'],
})
export class DeploymentStatusComponent implements OnInit {

  deploymentStatus: DeploymentStatusDetails;
  appFamily: any;
  applicationId: string;
  environment: string;
  environmentVariables = [];

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


  constructor(private activatedRoute: ActivatedRoute, private applicationControllerService: ApplicationControllerService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      this.appFamily = params.get('appFamily');
      this.applicationId = params.get('applicationId');
      this.environment = params.get('environment');
      this.loadDeploymentStatus();
    });
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
    });
  }

}
