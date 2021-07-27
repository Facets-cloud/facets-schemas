import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {AwsClusterRequest} from "../../../cc-api/models/aws-cluster-request";
import {NbDialogService, NbToastrService} from "@nebular/theme";
import {UiAwsClusterControllerService} from "../../../cc-api/services/ui-aws-cluster-controller.service";
import {ActivatedRoute} from "@angular/router";
import {AwsCluster} from "../../../cc-api/models/aws-cluster";
import {AbstractCluster} from "../../../cc-api/models/abstract-cluster";
import {UiStackControllerService} from "../../../cc-api/services/ui-stack-controller.service";

@Component({
  selector: 'app-cluster-vars',
  templateUrl: './cluster-vars.component.html',
  styleUrls: ['./cluster-vars.component.scss']
})
export class ClusterVarsComponent implements OnInit {

  enableSubmitForClusterOverrides = true;
  nonSensitiveClusterSource: LocalDataSource = new LocalDataSource();
  sensitiveClusterSource: LocalDataSource = new LocalDataSource();
  secretsChanged = {}
  settings = {
    columns: {
      name: {
        title: 'Name',
        filter: true,
        width: '30%',
        editable: false,
      },
      value: {
        title: 'Value',
        filter: false,
        width: '70%',
        editable: true,
        editor: {type: 'password'},
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
      position: 'right',
    },
    edit: {
      inputClass: '',
      editButtonContent: '<i class="eva-edit-outline eva"></i>',
      saveButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmSave: true,
    },
  };
  settingsSecrets = {
    columns: {
      name: {
        title: 'Name',
        filter: true,
        width: '30%',
        editable: false,
      },
      value: {
        title: 'Value',
        filter: false,
        width: '50%',
        editable: true,
        editor: {type: 'password'},
      },
      status: {
        title: 'Status',
        filter: false,
        width: '20%',
        editable: false,
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
      position: 'right',
    },
    edit: {
      inputClass: '',
      editButtonContent: '<i class="eva-edit-outline eva"></i>',
      saveButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmSave: true,
    },
  };
  addOverrideSpinner: boolean = false;
  cluster: AwsCluster;
  extraEnvVars = ['TZ', 'CLUSTER', 'AWS_REGION'];
  private stackName: any;
  originalData: any = {};
  objectKeys = Object.keys;
  varsChanged: any = {};
  private stackVars: any;

  constructor(private toastrService: NbToastrService, private aWSClusterService: UiAwsClusterControllerService,
              private route: ActivatedRoute, private dialogService: NbDialogService, private uiStackControllerService: UiStackControllerService) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {
      if (p.clusterId) {
        var clusterId = p.clusterId;
        this.uiStackControllerService.getStackUsingGET(p.stackName).subscribe(
          s => {
            this.stackVars = s.stackVars
            this.aWSClusterService.getClusterUsingGET2(clusterId).subscribe(t => {
              this.loadDataInTables(t, this.stackVars);
              this.cluster = t;
            });
          }
        )

      }

      this.stackName = p.stackName;
    });
  }

  validateSecrets(event) {
    if (event.newData.value != this.originalData[event.newData.name]) {
      this.secretsChanged[event.newData.name] = event.newData.value;
    } else {
      delete this.secretsChanged[event.newData.name]
    }
    event.confirm.resolve(event.newData);
  }

  validateVars(event) {
    if (event.newData.value != this.originalData[event.newData.name]) {
      this.varsChanged[event.newData.name] = event.newData.value;
    } else {
      delete this.varsChanged[event.newData.name]
    }
    event.confirm.resolve(event.newData);
  }

  /**
   * Load data in table datasources.
   * @param cluster
   */
  loadDataInTables(cluster: AbstractCluster, stackVars) {
    let dataSource = [];
    Object.keys(cluster.commonEnvironmentVariables).forEach(element => {
      if (this.extraEnvVars.includes(element) || element in stackVars) {
      } else {
        dataSource.push({name: element, value: cluster.commonEnvironmentVariables[element]});
        this.originalData[element] = cluster.commonEnvironmentVariables[element];
      }
    });
    this.nonSensitiveClusterSource.load(dataSource);

    dataSource = [];
    Object.keys(cluster.secrets).forEach(element => {
      dataSource.push({name: element, status: cluster.secrets[element], value: "*"});
      this.originalData[element] = cluster.commonEnvironmentVariables[element];
    });
    this.sensitiveClusterSource.load(dataSource);
  }

  async submitClusterOverrides(ref: any) {
    const awsClusterRequest: AwsClusterRequest = await this.constructUpdateClusterRequest();
    this.aWSClusterService.updateClusterUsingPUT2({
      request: awsClusterRequest,
      clusterId: this.cluster.id
    }).subscribe(c => {
        console.log(c);
        this.loadDataInTables(c, this.stackVars);
        this.cluster = c;
        this.addOverrideSpinner = false;
        this.toastrService.success('Updated cluster variables', 'Success');
        ref.close();
      },
      e => {
        console.log(e);
        this.addOverrideSpinner = false;
        this.toastrService.danger('Update cluster variables Failed: ' + e.error.message, 'Error');
        ref.close();
      });

  }

  private async constructUpdateClusterRequest() {
    let awsClusterRequest: AwsClusterRequest = {};
    awsClusterRequest = {};
    awsClusterRequest.cloud = this.cluster.cloud;
    awsClusterRequest.clusterName = this.cluster.name;
    awsClusterRequest.stackName = this.cluster.stackName;
    awsClusterRequest.componentVersions = this.cluster.componentVersions;
    awsClusterRequest.clusterVars = {...this.secretsChanged, ...this.varsChanged};
    return awsClusterRequest;
  }


  openConfirmation(confirmation) {
    this.dialogService.open(confirmation, {})
  }

  clearValue($event: any) {
    debugger
    console.log($event)

  }
}
