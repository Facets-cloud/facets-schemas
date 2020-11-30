import { AwsClusterRequest } from './../../../cc-api/models/aws-cluster-request';
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { NbToastrService, NbStepperComponent, NbSelectModule } from '@nebular/theme';

@Component({
  selector: 'app-cluster-create',
  templateUrl: './cluster-create.component.html',
  styleUrls: ['./cluster-create.component.scss']
})
export class ClusterCreateComponent implements OnInit {

  awsClusterRequest: AwsClusterRequest = {
    clusterName: '',
  };

  awsRegions = ['GovCloud' , 'US_GOV_EAST_1' , 'US_EAST_1' , 'US_EAST_2' , 'US_WEST_1' , 'US_WEST_2' , 'EU_WEST_1' , 'EU_WEST_2' , 'EU_WEST_3' , 'EU_CENTRAL_1' , 'EU_NORTH_1' , 'AP_EAST_1' , 'AP_SOUTH_1' , 'AP_SOUTHEAST_1' , 'AP_SOUTHEAST_2' , 'AP_NORTHEAST_1' , 'AP_NORTHEAST_2' , 'SA_EAST_1' , 'CN_NORTH_1' , 'CN_NORTHWEST_1', 'CA_CENTRAL_1'];
  constructor() { }

  ngOnInit() {
  }

}
