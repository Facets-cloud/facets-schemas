import {Stack} from './../../../cc-api/models/stack';
import {UiAwsClusterControllerService, UiStackControllerService} from 'src/app/cc-api/services';
import {AwsClusterRequest} from './../../../cc-api/models/aws-cluster-request';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AwsCluster} from '../../../cc-api/models/aws-cluster';
import {NbToastrService} from '@nebular/theme';
import {AbstractCluster} from 'src/app/cc-api/models';

@Component({
  selector: 'app-cluster-create',
  templateUrl: './cluster-create.component.html',
  styleUrls: ['./cluster-create.component.scss']
})
export class ClusterCreateComponent implements OnInit {
  regionValues: { label: string; value: string }[];
  clusterListValues: { label: string; value: string }[];
  azsCsv: string;
  constructor(private clusterController: UiAwsClusterControllerService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService,
              private uiStackControllerService: UiStackControllerService,
              private stackController: UiStackControllerService) { }


  clusterList: any;
  stackName: any;
  stack: Stack;
  cluster: AwsCluster = null;
  cronScheduleModelBound: any;
  regionModelBound: any = 'US_EAST_1';
  timeZoneModelBound: any;
  originalClusterVariablesSource = [];
  awsClusterRequest: AwsClusterRequest = {
    clusterName: '',
    cloud: 'AWS',
    azs: [],
    clusterVars: {},
    tz: {},
    region: null,
    instanceTypes: null,
    componentVersions: {}
  };
  spotInstanceTypes: string;
  extraEnvVars = ['TZ', 'CLUSTER', 'AWS_REGION', 'sv4', 'sv5'];
  awsRegions = ['US_EAST_1' , 'US_EAST_2' , 'US_WEST_1' , 'US_WEST_2' ,
   'EU_WEST_1' , 'EU_WEST_2' , 'EU_WEST_3' , 'EU_CENTRAL_1' , 'EU_NORTH_1' ,
    'AP_EAST_1' , 'AP_SOUTH_1' , 'AP_SOUTHEAST_1' , 'AP_SOUTHEAST_2' , 'AP_NORTHEAST_1' ,
     'AP_NORTHEAST_2' , 'SA_EAST_1' , 'CN_NORTH_1' , 'CN_NORTHWEST_1', 'CA_CENTRAL_1', 'GovCloud' ,
      'US_GOV_EAST_1' ];


   dataSourceForSecrets: any = [];
   dataSourceForCommonVars: any = [];

  ngOnInit() {
    this.activatedRoute.params.subscribe(p => {
      this.stackName = p.stackName;
      if (p.clusterId) {
        this.clusterController.getClusterUsingGET2(p.clusterId).subscribe(clusterObj => {
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
      this.regionValues = this.awsRegions.map(x => {
        return {
          "value": x,
          "label": x.replace("_"," ").replace("_"," ")
        }
      });
    });

    this.uiStackControllerService.getClustersUsingGET1(this.stackName).subscribe(
      c => {
        this.clusterList = c;
        this.clusterListValues = this.clusterList.map(c=>{return {"value": c.id, "label": c.name}})
      }
    );
  }


  loadClusterVarsFromStack(stackObj: Stack) {
    Object.keys(stackObj.clusterVariablesMeta).forEach(key => {
      let element = stackObj.clusterVariablesMeta[key];
      if (element.secret) {
        this.dataSourceForSecrets.push({name: key, value: element.value, required: element.required});
      } else {
        this.dataSourceForCommonVars.push({name: key, value: element.value, required: element.required});
      }
    });
  }

  loadClusterVarsFromCluster(cluster: AbstractCluster){
debugger;
    Object.keys(cluster.commonEnvironmentVariables).forEach(element => {
      if (!this.extraEnvVars.includes(element)) {
      this.dataSourceForCommonVars.push({name: element, value: cluster.commonEnvironmentVariables[element]});
      const clone = {name: element, value: cluster.commonEnvironmentVariables[element]};
      this.originalClusterVariablesSource.push(clone);
      }
    });

    Object.keys(cluster.secrets).forEach(element => {
        this.dataSourceForSecrets.push({name: element, value: cluster.secrets[element]});
        const clone = {name: element, value: cluster.secrets[element]};
        this.originalClusterVariablesSource.push(clone);
      });
  }
  initAWSClusterRequestObject() {
    debugger;
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
    this.awsClusterRequest.accessKeyId = this.cluster.accessKeyId;
    this.awsClusterRequest.secretAccessKey = this.cluster.secretAccessKey;
    this.awsClusterRequest.k8sRequestsToLimitsRatio = this.cluster.k8sRequestsToLimitsRatio;
    this.awsClusterRequest.requireSignOff = this.cluster.requireSignOff;
    this.awsClusterRequest.schedules = this.cluster.schedules;
    this.awsClusterRequest.instanceTypes = this.cluster.instanceTypes;
    this.awsClusterRequest.componentVersions = this.cluster.componentVersions;
    this.regionModelBound = this.cluster.awsRegion.toUpperCase().replace('-', '_').replace('-', '_');
    this.spotInstanceTypes = this.cluster.instanceTypes.join(",");
    this.azsCsv = this.cluster.azs ? this.cluster.azs.join(","):"";
    this.cronScheduleModelBound = this.cluster.schedules.RELEASE;
  }

  async createCluster() {
    this.awsClusterRequest.stackName = this.stackName;
    this.awsClusterRequest.schedules = {RELEASE: this.cronScheduleModelBound};
    this.awsClusterRequest.region = this.regionModelBound;
    this.awsClusterRequest.tz = this.timeZoneModelBound;
    this.awsClusterRequest.instanceTypes = this.spotInstanceTypes.split(",");
    this.awsClusterRequest.azs = this.azsCsv.split(",")

    this.dataSourceForCommonVars.forEach(element => {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
    });
    const secretsDataSource = this.dataSourceForSecrets;
    secretsDataSource.forEach(element => {
      if(element.value!="****") {
        this.awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });

    try {
      debugger
      this.clusterController.createClusterUsingPOST2(this.awsClusterRequest)
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

  isChinaRegion() {
    return this.regionModelBound === 'CN_NORTH_1' || this.regionModelBound === 'CN_NORTHWEST_1';
  }

  async updateCluster(){
    this.awsClusterRequest.stackName = this.stackName;
    this.awsClusterRequest.schedules = {RELEASE: this.cronScheduleModelBound};
    this.awsClusterRequest.region = this.regionModelBound;
    this.awsClusterRequest.tz = this.timeZoneModelBound;
    this.awsClusterRequest.instanceTypes = this.spotInstanceTypes.split(",");

    this.dataSourceForCommonVars.forEach(element => {
      if (this.hasClusterVariableChanged(this.dataSourceForCommonVars, element.name)) {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });
    const secretsDataSource = await this.dataSourceForSecrets;
    secretsDataSource.forEach(element => {
      if (this.hasClusterVariableChanged(secretsDataSource, element.name)) {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });

    try {
      this.clusterController.updateClusterUsingPUT2({
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

  test(form) {
    console.log(form.valid)
    return true;
  }
}
