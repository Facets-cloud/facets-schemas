import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {NbToastrService} from "@nebular/theme";
import {UiStackControllerService} from "../../../cc-api/services/ui-stack-controller.service";
import {UiLocalClusterControllerService} from "../../../cc-api/services/ui-local-cluster-controller.service";
import {LocalCluster} from "../../../cc-api/models/local-cluster";
import {Stack} from "../../../cc-api/models/stack";
import {ClusterCreateHelperService} from "../../../services/cluster-create-helper.service";
import {LocalClusterRequest} from "../../../cc-api/models/local-cluster-request";
import {TimeZone} from "../../../cc-api/models/time-zone";

@Component({
  selector: 'app-cluster-create-local',
  templateUrl: './cluster-create-local.component.html',
  styleUrls: ['./cluster-create-local.component.scss']
})
export class ClusterCreateLocalComponent implements OnInit {
  stackName: any;
  cluster: LocalCluster;
  stack: Stack;
  dataSourceForSecrets: any = [];
  dataSourceForCommonVars: any = [];
  extraEnvVars = ['TZ', 'CLUSTER', 'AWS_REGION', 'sv4', 'sv5'];
  awsClusterRequest: LocalClusterRequest = {
    cloud: "LOCAL",
    clusterName: "",
    clusterVars: {},
    releaseStream: "QA",
    schedules: {}
  };

  constructor(private clusterController: UiLocalClusterControllerService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService,
              private stackController: UiStackControllerService,
              private clusterCreateHelperService: ClusterCreateHelperService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(p => {
      this.stackName = p.stackName;
      if (p.clusterId) {
        this.clusterController.getClusterUsingGET3(p.clusterId).subscribe(clusterObj => {
          this.cluster = clusterObj;
          this.clusterCreateHelperService.loadClusterVarsFromCluster(clusterObj, this.dataSourceForSecrets, this.dataSourceForCommonVars, this.extraEnvVars);
        });
      } else {
        this.stackController.getStackUsingGET(this.stackName).subscribe(
          s => {
            this.clusterCreateHelperService.loadClusterVarsFromStack(s, this.dataSourceForCommonVars,this.dataSourceForSecrets);
            this.stack = s;
            console.log(this.stack);
          });
      }
    });
  }

  createCluster() {

  }

  updateCluster() {

  }
}
