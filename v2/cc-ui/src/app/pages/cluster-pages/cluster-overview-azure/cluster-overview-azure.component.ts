import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NbDialogService, NbToastrService } from '@nebular/theme';
import { LocalDataSource } from 'ng2-smart-table';
import { AbstractCluster, AzureCluster, AzureClusterRequest, SimpleOauth2User } from 'src/app/cc-api/models';
import { ApplicationControllerService, UiAzureClusterControllerService, UiCommonClusterControllerService, UiStackControllerService } from 'src/app/cc-api/services';
import flat from 'flat';
import { ComponentUpgradeDialogComponent } from '../cluster-overview/componentupgradedialog/componentupgradedialog.component';

@Component({
  selector: 'app-cluster-overview-azure',
  templateUrl: './cluster-overview-azure.component.html',
  styleUrls: ['./cluster-overview-azure.component.scss']
})
export class ClusterOverviewAzureComponent implements OnInit {

  downloadingKubeConfig = false;
  releaseTypes = ['Release', 'Hotfix'];
  releaseTypeSelection: any = 'Release';
  applicationName: any = '';
  payload: any = '{}';
  user: SimpleOauth2User;

  settings = {
    columns: {
      name: {
        title: 'Variable Name',
        filter: false,
        width: '30%',
        editable: false,
      },
      value: {
        title: 'Variable Value',
        filter: false,
        width: '70%',
        editable: true,
        editor: {type: 'text'},
      }
    },
    noDataMessage: '',
    pager: {
      display: true,
      perPage: 10,
    },
    actions: {
      add: false,
      edit: true,
      delete: false,
      // class: "my-custom-class",
      // custom: [
      //   { name: 'editrecord', title: '<i>&#9986;</i>'},
      //   { name: 'deleterecord', title: '<i>&#9998;</i>' }
      // ],
      position: 'right',
    },
    edit: {
      inputClass: '',
      editButtonContent: '<i class="eva-edit-outline eva"></i>',
      saveButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmSave: false,
    },
  };

  settingsComponentUpgrades = {
    columns: {
      component: {
        title: 'Component',
        filter: false,
        width: '30%',
        editable: false,
      },
      currentValue: {
        title: 'Current Version',
        filter: false,
        width: '30%',
        editable: false,
        editor: {type: 'text'},
      },
      newValue: {
        title: 'Available Version',
        filter: false,
        width: '30%',
        editable: false,
        editor: {type: 'text'},
      }
    },
    noDataMessage: '',
    pager: {
      display: true,
      perPage: 10,
    },
    actions: {
      columnTitle: 'Upgrade',
      add: false,
      edit: false,
      delete: false,
      custom: [
        { name: 'upgradeComponent', title: '<div style="padding-top: 25px;"><i class="eva-arrow-upward-outline eva"></i></div>'},
      ],
      position: 'right',
    }
  };

  clusterInfo;

  cluster: AzureCluster;
  spotInstanceTypes: string = null;

  enableSubmitForClusterOverrides = true;
  nonSensitiveClusterSource: LocalDataSource = new LocalDataSource();
  sensitiveClusterSource: LocalDataSource = new LocalDataSource();
  componentUpgradeSource: LocalDataSource = new LocalDataSource();
  originalClusterVariablesSource = [];
  addOverrideSpinner = false;
  stackName: string;
  extraEnvVars = ['TZ', 'CLUSTER', 'AWS_REGION'];
  isUserAdmin: any;
  shouldUpgradeComponent = false;
  clusterComponents: {[key: string]: string}

  constructor(private azureClusterService: UiAzureClusterControllerService,
              private uiCommonClusterControllerService: UiCommonClusterControllerService,
              private route: ActivatedRoute,
              private router: Router,
              private dialogService: NbDialogService,
              private toastrService: NbToastrService,
              private stackService: UiStackControllerService,
              private applicationController: ApplicationControllerService) {
  }

  ngOnInit(): void {
    let clusterId = '';
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        clusterId = p.clusterId;
        this.azureClusterService.getAzureClusterUsingGET(clusterId).subscribe(t => {
          this.updateTableSourceWithStackVariables(t);
          this.cluster = t;
          this.spotInstanceTypes = null;
          if(this.cluster.instanceTypes != null){
            this.spotInstanceTypes = this.cluster.instanceTypes.join(",");
          }
          this.clusterInfo = flat.flatten(t);
          this.populateComponentUpgrades(t)
        });
      }

      this.stackName = p.stackName;
    });
    this.applicationController.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
        this.isUserAdmin = (this.user.authorities.map(x => x.authority).includes('ROLE_ADMIN'))
        || this.user.authorities.map(x => x.authority).includes('ROLE_USER_ADMIN');
      }
    );
  }

  async upgradeComponent(event) {
    let data = event.data;
    const awsClusterRequest: AzureClusterRequest = await this.constructUpdateClusterRequest();
    this.dialogService.open(ComponentUpgradeDialogComponent, {context: {component: data["component"], version: data["newValue"]}}).onClose.subscribe(proceed => {
      if (proceed) {
        awsClusterRequest.componentVersions[data['component']] = data['newValue'];
        this.updateCluster(awsClusterRequest);
      }
    });
  }

  populateComponentUpgrades(cluster: AbstractCluster) {
    this.stackService.getStackUsingGET(cluster.stackName).subscribe(stack => {
      this.uiCommonClusterControllerService.getClusterTaskUsingGET(cluster.id).subscribe(clusterTasks => {
        if (clusterTasks && clusterTasks.tasks && (clusterTasks.tasks.length > 0)) {
          this.componentUpgradeSource.reset();
          this.componentUpgradeSource.load([]);
          return;
        }

        let componentUpgrades = [];
        for (let clusterKey in cluster.componentVersions) {
          let clusterValue = parseFloat(cluster.componentVersions[clusterKey]);

          let stackValue = stack.componentVersions[clusterKey];
          if (clusterKey === "KUBERNETES") {
            let k8sStackValue = parseFloat(stackValue);
            if (k8sStackValue > clusterValue) {
              componentUpgrades.push({
                "component": clusterKey,
                "currentValue": clusterValue.toString(),
                "newValue": (clusterValue + 0.01).toString()})
            }
          }

        }
        this.componentUpgradeSource.load(componentUpgrades);
      })
    });
  }

  updateTableSourceWithStackVariables(cluster: AbstractCluster) {
    let dataSource = [];
    Object.keys(cluster.commonEnvironmentVariables).forEach(element => {
      if (!this.extraEnvVars.includes(element)) {
        dataSource.push({name: element, value: cluster.commonEnvironmentVariables[element]});
        const clone = {name: element, value: cluster.commonEnvironmentVariables[element]};
        this.originalClusterVariablesSource.push(clone);
      }
    });
    this.nonSensitiveClusterSource.load(dataSource);

    dataSource = [];
    Object.keys(cluster.secrets).forEach(element => {
      dataSource.push({name: element, value: cluster.secrets[element]});
      const clone = {name: element, value: cluster.secrets[element]};
      this.originalClusterVariablesSource.push(clone);
    });
    this.sensitiveClusterSource.load(dataSource);
  }

  validateValue(event) {
    console.log(event);
    event.confirm.resolve(event.newData);
  }

  async submitClusterOverrides() {
    this.enableSubmitForClusterOverrides = true;
    const awsClusterRequest: AzureClusterRequest = await this.constructUpdateClusterRequest();
    try {
      if (this.isEmptyObject(awsClusterRequest.clusterVars)) {
        this.toastrService.danger('No variables have changed, cannot update', 'Error');
        this.addOverrideSpinner = false;
        return;
      }
      this.updateCluster(awsClusterRequest);
    } catch (err) {
      console.log(err);
      this.addOverrideSpinner = false;
      // this.toastrService.danger('Error updating stack variables', 'Error');
    }
  }

  errorHandler(error){
    this.addOverrideSpinner = false;
    this.toastrService.warning(error.error.message, 'Error');
  }


  refreshStack() {
    this.addOverrideSpinner = true;
    try {
      this.stackService.reloadStackUsingGET1(this.cluster.stackName).subscribe(stack => {
        this.addOverrideSpinner = false;
        location.reload();
      },
      err => {
        this.errorHandler(err);
      });
    } catch (err) {
      console.log(err);
      this.addOverrideSpinner = false;
    }
  }

  private resetClusterOverrides() {
    this.addOverrideSpinner = false;
    this.nonSensitiveClusterSource.reset();
    this.sensitiveClusterSource.reset();
  }

  private hasClusterVariableChanged(source, variableName: string) {
    let originalValue = null;
    this.originalClusterVariablesSource.forEach(element => {
      if (element.name === variableName) {
        originalValue = element.value;
      }
    });

    let newValue = null;
    source.forEach(element => {
      if (element.name === variableName) {
        newValue = element.value;
      }
    });

    if (originalValue && newValue != originalValue) {
      return true;
    }

    return false;
  }

  isEmptyObject(obj: Object) {
    return (obj && (Object.keys(obj).length === 0));
  }

  private async updateCluster(awsClusterRequest: AzureClusterRequest) {
    this.addOverrideSpinner = true;

    try {
      this.azureClusterService.updateAzureClusterUsingPUT({
        request: awsClusterRequest,
        clusterId: this.cluster.id
      }).subscribe(c => {
        console.log(c);
        this.resetClusterOverrides();
        this.addOverrideSpinner = false;
        this.toastrService.success('Updated cluster variables', 'Success');
        location.reload();
      });
      } catch (err) {
      console.log(err);
      this.addOverrideSpinner = false;
      this.toastrService.danger('Cannot update stack', 'Error');
    }

    console.log(awsClusterRequest);
  }

  private async constructUpdateClusterRequest() {
    let awsClusterRequest: AzureClusterRequest = {};

    awsClusterRequest = {};
    awsClusterRequest.cloud = this.cluster.cloud;
    awsClusterRequest.clusterName = this.cluster.name;
    awsClusterRequest.stackName = this.cluster.stackName;
    awsClusterRequest.componentVersions = this.cluster.componentVersions;
    awsClusterRequest.clusterVars = {};

    const nonSensitiveSource = await this.nonSensitiveClusterSource.getAll();
    nonSensitiveSource.forEach(element => {
      if (this.hasClusterVariableChanged(nonSensitiveSource, element.name)) {
        awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });

    const sensitiveSource = await this.sensitiveClusterSource.getAll();
    sensitiveSource.forEach(element => {
      if (this.hasClusterVariableChanged(sensitiveSource, element.name)) {
        awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });

    return awsClusterRequest;
  }

  downloadKubeConfig() {
    this.downloadingKubeConfig = true;
    this.uiCommonClusterControllerService.getKubeConfigUsingGET(this.cluster.id).subscribe(
      x => {
        const blob = new Blob([x], { type: 'plain/text' });
        const url = window.URL.createObjectURL(blob);
        var anchor = document.createElement("a");
        anchor.download = this.cluster.name + "-kubeconfig";
        anchor.href = url;
        anchor.click();
        this.downloadingKubeConfig = false;
      }
    );
  }

  refreshKubeconfig() {
    this.downloadingKubeConfig = true;
    this.uiCommonClusterControllerService.refreshKubeConfigUsingGET(this.cluster.id).subscribe(
      x => {
        this.downloadingKubeConfig = false;
        this.toastrService.success("Permissions renewed for 24h", "Success");
      }
    )
  }

}
