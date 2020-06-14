import {Component, OnInit} from '@angular/core';
import {UiCommonClusterControllerService} from '../../../cc-api/services/ui-common-cluster-controller.service';
import {ActivatedRoute} from '@angular/router';
import {OverrideObject} from '../../../cc-api/models/override-object';

@Component({
  selector: 'app-cluster-overrides',
  templateUrl: './cluster-overrides.component.html',
  styleUrls: ['./cluster-overrides.component.scss']
})
export class ClusterOverridesComponent implements OnInit {
  private clusterId: any;
  private overrides: OverrideObject[];


  constructor(private clusterService: UiCommonClusterControllerService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(p => {
      if (!p.clusterId) {
        console.log(p);
      }
      this.clusterId = p.clusterId;
      this.clusterService.getOverridesUsingGET1(p.clusterId).subscribe(
        res => this.overrides = res
      );
    });

  }

}
