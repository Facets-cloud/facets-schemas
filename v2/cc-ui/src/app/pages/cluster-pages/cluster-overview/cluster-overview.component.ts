import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AwsCluster} from '../../../cc-api/models/aws-cluster';
import {UiAwsClusterControllerService} from '../../../cc-api/services/ui-aws-cluster-controller.service';
import flat from 'flat';

@Component({
  selector: 'app-cluster-overview',
  templateUrl: './cluster-overview.component.html',
  styleUrls: ['./cluster-overview.component.scss']
})
export class ClusterOverviewComponent implements OnInit {

  clusterInfo;

  constructor(private aWSClusterService: UiAwsClusterControllerService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    let clusterId = '';
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        clusterId = p.clusterId;
        this.aWSClusterService.getClusterUsingGET1(clusterId).subscribe(
          t => this.clusterInfo = flat.flatten(t)
        );
      }
    });
  }

}
