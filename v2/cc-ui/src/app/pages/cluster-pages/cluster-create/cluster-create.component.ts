import { TimeZone } from './../../../cc-api/models/time-zone';
import { UiAwsClusterControllerService } from 'src/app/cc-api/services';
import { AwsClusterRequest } from './../../../cc-api/models/aws-cluster-request';
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { NbToastrService, NbStepperComponent, NbSelectModule } from '@nebular/theme';
import {AwsCluster} from '../../../cc-api/models/aws-cluster';
import { request } from 'http';
import {SimpleOauth2User} from '../../../cc-api/models/simple-oauth-2user';
import {ApplicationControllerService } from '../../../cc-api/services/application-controller.service';

@Component({
  selector: 'app-cluster-create',
  templateUrl: './cluster-create.component.html',
  styleUrls: ['./cluster-create.component.scss']
})
export class ClusterCreateComponent implements OnInit {
  constructor(private clusterController: UiAwsClusterControllerService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private applicationController: ApplicationControllerService) { }

  tzObj: TimeZone;
  clusterId: any;
  user: SimpleOauth2User;
  stackName: any;
  cronSchedule: any;
  cluster: AwsCluster;
  clusterVariables: any = '{\"key\": \"value\"}';
  isUserAdmin: any;
  awsClusterRequest: AwsClusterRequest = {
    clusterName: '',
    cloud: 'AWS',
    azs: []
  };

  awsRegions = ['US_EAST_1' , 'US_EAST_2' , 'US_WEST_1' , 'US_WEST_2' , 'EU_WEST_1' , 'EU_WEST_2' , 'EU_WEST_3' , 'EU_CENTRAL_1' , 'EU_NORTH_1' , 'AP_EAST_1' , 'AP_SOUTH_1' , 'AP_SOUTHEAST_1' , 'AP_SOUTHEAST_2' , 'AP_NORTHEAST_1' , 'AP_NORTHEAST_2' , 'SA_EAST_1' , 'CN_NORTH_1' , 'CN_NORTHWEST_1', 'CA_CENTRAL_1', 'GovCloud' , 'US_GOV_EAST_1' ];

  ngOnInit() {

    this.activatedRoute.params.subscribe(p => {
      if (p.clusterId) {
        this.clusterId = p.clusterId;
        this.clusterController.getClusterUsingGET1(this.clusterId).subscribe(clusterObj => {
          this.cluster = clusterObj;
          this.initializeClusterRequest();
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

  initializeClusterRequest() {
    this.awsClusterRequest.clusterName = this.cluster.name;
    this.awsClusterRequest.cloud = this.cluster.cloud;
    this.tzObj.displayName = this.cluster.tz;
    this.awsClusterRequest.tz = this.tzObj;
    this.awsClusterRequest.releaseStream = this.cluster.releaseStream;
    this.awsClusterRequest.cdPipelineParent = this.cluster.cdPipelineParent;
    this.awsClusterRequest.azs = this.cluster.azs;
    this.awsClusterRequest.vpcCIDR = this.cluster.vpcCIDR;
    this.awsClusterRequest.externalId = this.cluster.externalId;
    this.awsClusterRequest.roleARN = this.cluster.roleARN;
    this.awsClusterRequest.k8sRequestsToLimitsRatio = this.cluster.k8sRequestsToLimitsRatio;
    this.awsClusterRequest.requireSignOff = this.cluster.requireSignOff;
    this.awsClusterRequest.schedules = this.cluster.schedules;
    console.log(this.awsClusterRequest);
  }

  createCluster() {
    this.awsClusterRequest.schedules = {RELEASE: this.cronSchedule};
    this.awsClusterRequest.clusterVars = {};
    this.awsClusterRequest.stackName = this.stackName;
    this.awsClusterRequest.clusterVars = JSON.parse(this.clusterVariables);
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

}
