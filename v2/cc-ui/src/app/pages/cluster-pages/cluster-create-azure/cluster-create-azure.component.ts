import {Stack} from '../../../cc-api/models/stack';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NbToastrService} from '@nebular/theme';
import {ClusterCreateHelperService} from "../../../services/cluster-create-helper.service";
import { AzureCluster, AzureClusterRequest } from 'src/app/cc-api/models';
import { UiAzureClusterControllerService } from 'src/app/cc-api/services/ui-azure-cluster-controller.service';
import { UiStackControllerService } from 'src/app/cc-api/services';

@Component({
  selector: 'app-azure-cluster-create',
  templateUrl: './cluster-create-azure.component.html',
  styleUrls: ['./cluster-create-azure.component.scss']
})
export class AzureClusterCreateComponent implements OnInit {
  regionValues: { label: string; value: string }[];
  clusterListValues: { label: string; value: string }[];
  azsCsv: string;

  constructor(private clusterController: UiAzureClusterControllerService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService,
              private uiStackControllerService: UiStackControllerService,
              private stackController: UiStackControllerService,
              private clusterCreateHelperService: ClusterCreateHelperService) {
  }


  stackName: any;
  stack: Stack;
  cluster: AzureCluster = null;
  cronScheduleModelBound: any;
  regionModelBound: any = 'eastus2';
  timeZoneModelBound: any;
  originalClusterVariablesSource = [];
  azureClusterRequest: AzureClusterRequest = {
    clusterName: '',
    cloud: 'AZURE',
    azs: [],
    clusterVars: {},
    tz: {},
    region: null,
    instanceTypes: null,
    componentVersions: {}
  };
  spotInstanceTypes: string;
  extraEnvVars = ['TZ', 'CLUSTER'];
  regions = ['eastus2'];


  ngOnInit() {
    this.activatedRoute.params.subscribe(p => {
      this.stackName = p.stackName;
      if (p.clusterId) {
        this.clusterController.getAzureClusterUsingGET(p.clusterId).subscribe(clusterObj => {
          this.cluster = clusterObj;
          this.initAzureClusterRequestObject();
        });
      } else {
        this.stackController.getStackUsingGET(this.stackName).subscribe(
          s => {
            this.stack = s;
            console.log(this.stack);
          });
      }
      this.regionValues = this.regions.map(x => {
        return {
          "value": x,
          "label": x
        }
      });
    });

    this.uiStackControllerService.getClustersUsingGET1(this.stackName).subscribe(
      c1 => {
        this.clusterListValues = c1.map(c => {
          return {"value": c.id, "label": c.name}
        })
      }
    );
  }

  initAzureClusterRequestObject() {
    this.azureClusterRequest.clusterName = this.cluster.name;
    this.azureClusterRequest.cloud = this.cluster.cloud;
    this.timeZoneModelBound = this.cluster.tz;
    this.azureClusterRequest.tz.displayName = this.timeZoneModelBound;
    this.azureClusterRequest.releaseStream = this.cluster.releaseStream;
    this.azureClusterRequest.cdPipelineParent = this.cluster.cdPipelineParent;
    this.azureClusterRequest.azs = this.cluster.azs;
    this.azureClusterRequest.vpcCIDR = this.cluster.vpcCIDR;
    this.azureClusterRequest.subscriptionId = this.cluster.subscriptionId;
    this.azureClusterRequest.tenantId = this.cluster.tenantId;
    this.azureClusterRequest.clientId = this.cluster.clientId;
    this.azureClusterRequest.clientSecret = this.cluster.clientSecret;
    this.azureClusterRequest.k8sRequestsToLimitsRatio = this.cluster.k8sRequestsToLimitsRatio;
    this.azureClusterRequest.requireSignOff = this.cluster.requireSignOff;
    this.azureClusterRequest.schedules = this.cluster.schedules;
    this.azureClusterRequest.instanceTypes = this.cluster.instanceTypes;
    this.azureClusterRequest.componentVersions = this.cluster.componentVersions;
    this.regionModelBound = this.cluster.region;
    this.spotInstanceTypes = this.cluster.instanceTypes.join(",");
    this.azsCsv = this.cluster.azs ? this.cluster.azs.join(",") : "";
    this.cronScheduleModelBound = this.cluster.schedules.RELEASE;
  }

  async createCluster() {
    this.azureClusterRequest.stackName = this.stackName;
    this.azureClusterRequest.schedules = {RELEASE: this.cronScheduleModelBound};
    this.azureClusterRequest.region = this.regionModelBound;
    this.azureClusterRequest.tz = this.timeZoneModelBound;
    this.azureClusterRequest.instanceTypes = this.spotInstanceTypes.split(",");
    this.azureClusterRequest.azs = this.azsCsv.split(",")

    try {

      this.clusterController.createAzureClusterUsingPOST(this.azureClusterRequest)
        .subscribe(cluster => {
            this.router.navigate(['/capc/', this.stackName, 'cluster', cluster.id]);
          },
          error => {
            this.toastrService.danger('Cluster creation failed ' + error.statusText, 'Error', {duration: 8000});
          });
    } catch (err) {
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

  async updateCluster() {
    this.azureClusterRequest.stackName = this.stackName;
    this.azureClusterRequest.schedules = {RELEASE: this.cronScheduleModelBound};
    this.azureClusterRequest.region = this.regionModelBound;
    this.azureClusterRequest.tz = this.timeZoneModelBound;
    this.azureClusterRequest.instanceTypes = this.spotInstanceTypes.split(",");
    

    try {
      this.clusterController.updateAzureClusterUsingPUT({
        request: this.azureClusterRequest,
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
