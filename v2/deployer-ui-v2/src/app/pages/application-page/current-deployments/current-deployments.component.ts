import { OnInit, Input, OnChanges, Component } from '@angular/core';
import { Application, EnvironmentMetaData, Deployment } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';
import { environment } from '../../../../environments/environment.prod';
import { NbDialogService } from '@nebular/theme';
import { MessageBus } from '../../../@core/message-bus';

@Component({
  selector: 'current-deployments',
  templateUrl: './current-deployments.component.html',
  styleUrls: ['./current-deployments.component.scss']
})
export class CurrentDeploymentsComponent implements OnInit, OnChanges {
  ngOnChanges(changes: import("@angular/core").SimpleChanges): void {
    this.ngOnInit();
  }

  @Input() application: Application;

  environments: EnvironmentMetaData[];
  deployments: Deployment[] = [];
  settings = {
    columns: {
      environment: {
        title: 'Cluster',
      },
      buildId: {
        title: 'Build Id',
      },
      deployedBy: {
        title: 'Deployed By',
      },
      podSize: {
        title: 'Pod Size',
      },
      configurations: {
        type: 'custom',
        renderComponent: ActionsColumn,
        title: 'Environemnt Variables',
      },
      minReplicas: {
        title: 'Min Replicas',
      },
      maxReplicas: {
        title: 'Max Replicas',
      },
      cpuThreshold: {
        title: 'CPU Threshold (%)',
      },
      timestamp: {
        title: 'Deployed At',
      },
    },
    actions: false,
    hideSubHeader: true,
  };

  constructor(private applicationControllerService: ApplicationControllerService, private messageBus: MessageBus) { }

  ngOnInit() {
    this.messageBus.deploymentTopic.subscribe(x => this.populateData());
    this.populateData();
  }

  populateData() {
    if (this.application.applicationFamily) {
      this.applicationControllerService.getEnvironmentMetaDataUsingGET(this.application.applicationFamily)
        .subscribe(x => {
          this.environments = x;
          this.loadLatestDeployments();
        });
    }
  }

  async loadLatestDeployments() {
    const deployments = [];
    for (const environment of this.environments) {
      const deployment: Deployment = await this.applicationControllerService.getCurrentDeploymentUsingGET({
        environment: environment.name,
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
      }).toPromise();
      if (deployment) {
        deployment['minReplicas'] = deployment.horizontalPodAutoscaler.minReplicas;
        deployment['maxReplicas'] = deployment.horizontalPodAutoscaler.maxReplicas;
        deployment['cpuThreshold'] = deployment.horizontalPodAutoscaler.threshold;
        deployments.push(deployment);
      }
    }
    this.deployments = deployments;
  }

}

@Component({
  selector: 'deployment-environment-variables-column',
  template: `
  <button style="width: 100%; margin: auto;" (click)="showDeploymentDetails()" nbTooltip="Show Environment Variables" nbButton appearance="ghost"><nb-icon pack="eva" icon="eye-outline"></nb-icon></button>
  `,
})
export class ActionsColumn {
  @Input() rowData: Deployment;

  constructor(private nbDialogService: NbDialogService) {
  }

  showDeploymentDetails() {
    this.nbDialogService.open(DeploymentDetailsDialog, { context: { deployment: this.rowData } });
  }
}

@Component({
  selector: 'deployment-details-dialog',
  templateUrl: './deployment-details-dialog.html',
})
export class DeploymentDetailsDialog {
  @Input() deployment: Deployment;
}
