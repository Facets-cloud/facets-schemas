import { Stack } from './../../../cc-api/models/stack';
import { UiAwsClusterControllerService, UiStackControllerService } from 'src/app/cc-api/services';
import { AwsClusterRequest } from './../../../cc-api/models/aws-cluster-request';
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AwsCluster} from '../../../cc-api/models/aws-cluster';
import {NbToastrService, NbSelectModule } from '@nebular/theme';
import {LocalDataSource} from 'ng2-smart-table';
import { AbstractCluster } from 'src/app/cc-api/models';

@Component({
  selector: 'app-cluster-create',
  templateUrl: './cluster-create.component.html',
  styleUrls: ['./cluster-create.component.scss']
})
export class ClusterCreateComponent implements OnInit {
  constructor(private clusterController: UiAwsClusterControllerService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService,
              private uiStackControllerService: UiStackControllerService,
              private stackController: UiStackControllerService) { }


  // tzObj: TimeZone = {
  //   displayName: ''
  // };
  clusterList: any;
  stackName: any;
  stack: Stack;
  cluster: AwsCluster = null;
  cronScheduleModelBound: any;
  regionModelBound: any;
  timeZoneModelBound: any;
  commonVariablesTable: LocalDataSource = new LocalDataSource();
  secretsTable: LocalDataSource = new LocalDataSource();
  originalClusterVariablesSource = [];
  awsClusterRequest: AwsClusterRequest = {
    clusterName: '',
    cloud: 'AWS',
    azs: [],
    clusterVars: {},
    tz: {},
    region: null
  };
  extraEnvVars = ['TZ', 'CLUSTER', 'AWS_REGION', 'sv4', 'sv5'];
  awsRegions = ['US_EAST_1' , 'US_EAST_2' , 'US_WEST_1' , 'US_WEST_2' ,
   'EU_WEST_1' , 'EU_WEST_2' , 'EU_WEST_3' , 'EU_CENTRAL_1' , 'EU_NORTH_1' ,
    'AP_EAST_1' , 'AP_SOUTH_1' , 'AP_SOUTHEAST_1' , 'AP_SOUTHEAST_2' , 'AP_NORTHEAST_1' ,
     'AP_NORTHEAST_2' , 'SA_EAST_1' , 'CN_NORTH_1' , 'CN_NORTHWEST_1', 'CA_CENTRAL_1', 'GovCloud' ,
      'US_GOV_EAST_1' ];

  settings = {
    columns: {
      name: {
        title: 'Name',
        filter: false,
        width: '50%',
        editable: false,
      },
      value: {
        title: 'Value',
        filter: false,
        width: '50%',
        editable: true,
        editor: {type: 'text'},
      }
    },
    noDataMessage: '',
    pager: {
      display: true,
      perPage: 5,
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
      confirmSave: false,
    },
  };

  ngOnInit() {

    this.activatedRoute.params.subscribe(p => {
      this.stackName = p.stackName;
      if (p.clusterId) {
        this.clusterController.getClusterUsingGET1(p.clusterId).subscribe(clusterObj => {
          this.cluster = clusterObj;
          this.initAWSClusterRequestObject();
          this.loadClusterVarsFromCluster(clusterObj);
        });
      }else {
        this.stackController.getStackUsingGET(this.stackName).subscribe(
          s => {
            this.loadClusterVarsFromStack(s);
            this.stack = s;
            console.log(this.stack);
          });
      }
    });

    this.uiStackControllerService.getClustersUsingGET1(this.stackName).subscribe(
      c => {
        this.clusterList = c;
      }
    );
  }


  loadClusterVarsFromStack(stackObj: Stack) {
    console.log('****** loading from Stack');
    let dataSourceForCommonVars = [];
    let dataSourceForSecrets = [];
    // Object.keys(stackObj.stackVars).forEach(ele => {
    //   dataSourceForCommonVars.push({name: ele, value: stackObj.stackVars[ele]});
    // });
    Object.keys(stackObj.clusterVariablesMeta).forEach(ele => {
      if (stackObj.clusterVariablesMeta[ele].secret){
        dataSourceForSecrets.push({name: ele, value: stackObj.clusterVariablesMeta[ele].value});
      }else{
        dataSourceForCommonVars.push({name: ele, value: stackObj.clusterVariablesMeta[ele].value});
    }
    });
    this.commonVariablesTable.load(dataSourceForCommonVars);
    this.secretsTable.load(dataSourceForSecrets);
  }

  loadClusterVarsFromCluster(cluster: AbstractCluster){
    console.log('****** loading from Cluster');
    let dataSourceForCommonVars = [];
    let dataSourceForSecrets = [];
    Object.keys(cluster.commonEnvironmentVariables).forEach(element => {
      if (!this.extraEnvVars.includes(element)) {
      dataSourceForCommonVars.push({name: element, value: cluster.commonEnvironmentVariables[element]});
      const clone = {name: element, value: cluster.commonEnvironmentVariables[element]};
      this.originalClusterVariablesSource.push(clone);
      }
    });
    this.commonVariablesTable.load(dataSourceForCommonVars);

    Object.keys(cluster.secrets).forEach(element => {
        dataSourceForSecrets.push({name: element, value: cluster.secrets[element]});
        const clone = {name: element, value: cluster.secrets[element]};
        this.originalClusterVariablesSource.push(clone);
      });
    this.secretsTable.load(dataSourceForSecrets);
  }
  initAWSClusterRequestObject() {
    this.awsClusterRequest.clusterName = this.cluster.name;
    this.awsClusterRequest.cloud = this.cluster.cloud;
    this.timeZoneModelBound = this.cluster.tz;
    this.awsClusterRequest.tz.displayName = this.timeZoneModelBound;
    this.awsClusterRequest.releaseStream = this.cluster.releaseStream;
    this.awsClusterRequest.cdPipelineParent = this.cluster.cdPipelineParent;
    this.awsClusterRequest.azs = this.cluster.azs;
    this.awsClusterRequest.vpcCIDR = this.cluster.vpcCIDR;
    this.awsClusterRequest.externalId = this.cluster.externalId;
    this.awsClusterRequest.roleARN = this.cluster.roleARN;
    this.awsClusterRequest.k8sRequestsToLimitsRatio = this.cluster.k8sRequestsToLimitsRatio;
    this.awsClusterRequest.requireSignOff = this.cluster.requireSignOff;
    this.awsClusterRequest.schedules = this.cluster.schedules;
    this.regionModelBound = this.cluster.awsRegion.toUpperCase().replace('-', '_').replace('-', '_');
    this.cronScheduleModelBound = this.cluster.schedules.RELEASE;
    console.log(this.cluster);
    console.log(this.awsClusterRequest);
  }

  async createCluster() {
    this.awsClusterRequest.stackName = this.stackName;
    this.awsClusterRequest.schedules = {RELEASE: this.cronScheduleModelBound};
    this.awsClusterRequest.region = this.regionModelBound;
    this.awsClusterRequest.tz = this.timeZoneModelBound;

    const commmonVarsDataSource = await this.commonVariablesTable.getAll();
    commmonVarsDataSource.forEach(element => {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
    });
    const secretsDataSource = await this.secretsTable.getAll();
    secretsDataSource.forEach(element => {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
    });

    console.log(this.awsClusterRequest);
    try {
    this.clusterController.createClusterUsingPOST1(this.awsClusterRequest)
    .subscribe(cluster => {
      this.router.navigate(['/capc/', this.stackName, 'cluster', cluster.id]);
    },
    error => {
      this.toastrService.danger('Cluster creation failed ' + error.statusText, 'Error', {duration: 8000});
    });
    }catch (err) {
      console.log(err);
      this.toastrService.danger('Cluster creation failed', 'Error', {duration: 5000});
    }
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

  async updateCluster(){
    this.awsClusterRequest.stackName = this.stackName;
    this.awsClusterRequest.schedules = {RELEASE: this.cronScheduleModelBound};
    this.awsClusterRequest.region = this.regionModelBound;
    this.awsClusterRequest.tz = this.timeZoneModelBound;

    const commmonVarsDataSource = await this.commonVariablesTable.getAll();
    commmonVarsDataSource.forEach(element => {
      if (this.hasClusterVariableChanged(commmonVarsDataSource, element.name)) {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });
    const secretsDataSource = await this.secretsTable.getAll();
    secretsDataSource.forEach(element => {
      if (this.hasClusterVariableChanged(secretsDataSource, element.name)) {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });

    try {
      this.clusterController.updateClusterUsingPUT1({
        request: this.awsClusterRequest,
        clusterId: this.cluster.id
      }).subscribe(c => {
        console.log(c);
        this.router.navigate(['/capc/', this.stackName, 'cluster', this.cluster.id]);
      },
      error => {
        this.toastrService.danger('Cluster update failed ' + error.statusText, 'Error', {duration: 8000});
      });
      } catch (err) {
      console.log(err);
      this.toastrService.danger('Cluster update failed ' + err.statusText, 'Error', {duration: 8000});
    }
  }

  validateValue(event) {
    console.log(event);
    event.confirm.resolve(event.newData);
  }

}
