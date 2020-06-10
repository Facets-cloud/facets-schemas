import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AwsCluster} from '../../cc-api/models/aws-cluster';
import {UiAwsClusterControllerService} from '../../cc-api/services/ui-aws-cluster-controller.service';
import flat from 'flat';

@Component({
  selector: 'app-cluster-overview',
  templateUrl: './cluster-overview.component.html',
  styleUrls: ['./cluster-overview.component.scss']
})
export class ClusterOverviewComponent implements OnInit {

  clusterInfo;

  constructor(private aWSClusterService: UiAwsClusterControllerService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    let clusterId = '';
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        clusterId = p.clusterId;
      }
    });
    if (!clusterId) {
      // handle error
    }
    this.aWSClusterService.getClusterUsingGET1(clusterId).subscribe(
      t => this.clusterInfo = flat.flatten(t)
    );
  }

}
