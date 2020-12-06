import { Stack } from './../../../cc-api/models/stack';
import { TimeZone } from './../../../cc-api/models/time-zone';
import { UiAwsClusterControllerService, UiStackControllerService } from 'src/app/cc-api/services';
import { AwsClusterRequest } from './../../../cc-api/models/aws-cluster-request';
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { NbToastrService, NbStepperComponent, NbSelectModule } from '@nebular/theme';
import {AwsCluster} from '../../../cc-api/models/aws-cluster';
import { request } from 'http';
import {SimpleOauth2User} from '../../../cc-api/models/simple-oauth-2user';
import {ApplicationControllerService } from '../../../cc-api/services/application-controller.service';
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
              private applicationController: ApplicationControllerService,
              private stackController: UiStackControllerService) { }


  tzObj: TimeZone = {
    displayName: ''
  };
  clusterId: any;
  user: SimpleOauth2User;
  stackName: any;
  cronSchedule: any;
  cluster: AwsCluster;
  clusterVariables: any = '{\"key\": \"value\"}';
  commonVariablesTable: LocalDataSource = new LocalDataSource();
  secretsTable: LocalDataSource = new LocalDataSource();
  isUserAdmin: any;
  stack: Stack;
  regionTemp: any;
  awsClusterRequest: AwsClusterRequest = {
    clusterName: '',
    cloud: 'AWS',
    azs: [],
    clusterVars: {},
    tz: this.tzObj,
    region: null
  };

  awsRegions = ['US_EAST_1' , 'US_EAST_2' , 'US_WEST_1' , 'US_WEST_2' , 'EU_WEST_1' , 'EU_WEST_2' , 'EU_WEST_3' , 'EU_CENTRAL_1' , 'EU_NORTH_1' , 'AP_EAST_1' , 'AP_SOUTH_1' , 'AP_SOUTHEAST_1' , 'AP_SOUTHEAST_2' , 'AP_NORTHEAST_1' , 'AP_NORTHEAST_2' , 'SA_EAST_1' , 'CN_NORTH_1' , 'CN_NORTHWEST_1', 'CA_CENTRAL_1', 'GovCloud' , 'US_GOV_EAST_1' ];

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
      if (p.clusterId) {
        this.clusterId = p.clusterId;
        this.clusterController.getClusterUsingGET1(this.clusterId).subscribe(clusterObj => {
          this.cluster = clusterObj;
          this.initializeClusterRequest();
          this.loadClusterVarsFromCluster(clusterObj);
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

    if (!this.clusterId){
    this.stackController.getStackUsingGET(this.stackName).subscribe(
      s => {
        this.loadClusterVarsFromStack(s);
        this.stack = s;
        console.log(this.stack);
      });
    }
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
      dataSourceForCommonVars.push({name: element, value: cluster.commonEnvironmentVariables[element]});
    });
    this.commonVariablesTable.load(dataSourceForCommonVars);

    Object.keys(cluster.secrets).forEach(element => {
        dataSourceForSecrets.push({name: element, value: cluster.commonEnvironmentVariables[element]});
      });
    this.secretsTable.load(dataSourceForSecrets);
  }

  initializeClusterRequest() {
    this.awsClusterRequest.clusterName = this.cluster.name;
    this.awsClusterRequest.cloud = this.cluster.cloud;
    this.tzObj.displayName = this.cluster.tz;
    this.awsClusterRequest.tz.displayName = this.tzObj.displayName;
    this.awsClusterRequest.releaseStream = this.cluster.releaseStream;
    this.awsClusterRequest.cdPipelineParent = this.cluster.cdPipelineParent;
    this.awsClusterRequest.azs = this.cluster.azs;
    this.awsClusterRequest.vpcCIDR = this.cluster.vpcCIDR;
    this.awsClusterRequest.externalId = this.cluster.externalId;
    this.awsClusterRequest.roleARN = this.cluster.roleARN;
    this.awsClusterRequest.k8sRequestsToLimitsRatio = this.cluster.k8sRequestsToLimitsRatio;
    this.awsClusterRequest.requireSignOff = this.cluster.requireSignOff;
    this.awsClusterRequest.schedules = this.cluster.schedules;
    this.regionTemp = this.cluster.awsRegion.toUpperCase().replace('-', '_').replace('-', '_');
    this.cronSchedule = this.cluster.schedules.RELEASE;
    console.log(this.cluster);
    console.log(this.awsClusterRequest);
  }

  async createCluster() {
    const commmonVarsDataSource = await this.commonVariablesTable.getAll();
    commmonVarsDataSource.forEach(element => {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
    });
    const secretsDataSource = await this.secretsTable.getAll();
    secretsDataSource.forEach(element => {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
    });
    this.awsClusterRequest.schedules = {RELEASE: this.cronSchedule};
    this.awsClusterRequest.stackName = this.stackName;
    this.awsClusterRequest.region = this.regionTemp;
    console.log(this.awsClusterRequest);
    try {
    this.clusterController.createClusterUsingPOST1(this.awsClusterRequest)
    .subscribe(cluster => {
      console.log(cluster.id);
      this.router.navigate(['/capc/', this.stackName, 'cluster', cluster.id]);
    });
    }catch (err) {
    console.log(err);
    }
  }

  updateCluster(){
    try {
      this.clusterController.updateClusterUsingPUT1({
        request: this.awsClusterRequest,
        clusterId: this.cluster.id
      }).subscribe(c => {
        console.log(c);
        this.router.navigate(['/capc/', this.stackName, 'cluster', this.cluster.id]);
      });
      } catch (err) {
      console.log(err);
    }
  }

  validateValue(event) {
    console.log(event);
    //event.confirm.resolve(event.newData);
  }

}
