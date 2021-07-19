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
          this.awsClusterRequest.clusterName = this.cluster.name
        });
      } else {
        this.stackController.getStackUsingGET(this.stackName).subscribe(
          s => {
            this.clusterCreateHelperService.loadClusterVarsFromStack(s, this.dataSourceForSecrets,this.dataSourceForCommonVars);
            this.stack = s;
          });
      }
    });
  }

  createCluster() {
    this.populateRequestObject();
    try {

      this.clusterController.createClusterUsingPOST3(this.awsClusterRequest)
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

  updateCluster() {
    this.populateRequestObject();
    try {
      this.clusterController.updateClusterUsingPUT3({
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

  private populateRequestObject() {
    this.awsClusterRequest.stackName = this.stackName;
    this.dataSourceForCommonVars.forEach(element => {
      this.awsClusterRequest.clusterVars[element.name] = element.value;
    });
    const secretsDataSource = this.dataSourceForSecrets;
    secretsDataSource.forEach(element => {
      if (element.value != "****") {
        this.awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });
  }
}
