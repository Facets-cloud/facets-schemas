import { UiAwsClusterControllerService } from 'src/app/cc-api/services';
import { AwsClusterRequest } from './../../../cc-api/models/aws-cluster-request';
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { NbToastrService, NbStepperComponent, NbSelectModule } from '@nebular/theme';
import { request } from 'http';

@Component({
  selector: 'app-cluster-create',
  templateUrl: './cluster-create.component.html',
  styleUrls: ['./cluster-create.component.scss']
})
export class ClusterCreateComponent implements OnInit {
  constructor(private clusterController: UiAwsClusterControllerService,
              private activatedRoute: ActivatedRoute) { }

  stackName: any;
  cronSchedule: any;
  awsClusterRequest: AwsClusterRequest = {
    clusterName: '',
    cloud: 'AWS',
    azs: []
  };

  awsRegions = ['US_EAST_1' , 'US_EAST_2' , 'US_WEST_1' , 'US_WEST_2' , 'EU_WEST_1' , 'EU_WEST_2' , 'EU_WEST_3' , 'EU_CENTRAL_1' , 'EU_NORTH_1' , 'AP_EAST_1' , 'AP_SOUTH_1' , 'AP_SOUTHEAST_1' , 'AP_SOUTHEAST_2' , 'AP_NORTHEAST_1' , 'AP_NORTHEAST_2' , 'SA_EAST_1' , 'CN_NORTH_1' , 'CN_NORTHWEST_1', 'CA_CENTRAL_1', 'GovCloud' , 'US_GOV_EAST_1' ];

  ngOnInit() {

    this.activatedRoute.paramMap.subscribe(params => {
      this.stackName = params.get('stackName');
    });
  }

  createCluster() {
    this.awsClusterRequest.schedules = {RELEASE: this.cronSchedule};
    this.awsClusterRequest.clusterVars = {};
    this.awsClusterRequest.stackName = this.stackName;
    console.log(this.awsClusterRequest);
    this.clusterController.createClusterUsingPOST1(this.awsClusterRequest)
    .subscribe(rs => {
      console.log(rs);
    });
  }

}
