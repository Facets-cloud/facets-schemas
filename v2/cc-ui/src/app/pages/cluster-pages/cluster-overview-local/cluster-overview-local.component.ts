import {Component, OnInit} from '@angular/core';
import {UiCommonClusterControllerService} from "../../../cc-api/services/ui-common-cluster-controller.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UiLocalClusterControllerService} from "../../../cc-api/services/ui-local-cluster-controller.service";

@Component({
  selector: 'app-cluster-overview-local',
  templateUrl: './cluster-overview-local.component.html',
  styleUrls: ['./cluster-overview-local.component.scss']
})
export class ClusterOverviewLocalComponent implements OnInit {
  cluster: any;
  private clusterId: any;
  s3Url: string = "";
  s3Err: any;

  constructor(private uiCommonClusterControllerService: UiCommonClusterControllerService,
              private uiLocalClusterControllerService: UiLocalClusterControllerService,
              private route: ActivatedRoute,
              private router: Router,) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        this.clusterId = p.clusterId;
        this.uiCommonClusterControllerService.getClusterCommonUsingGET(p.clusterId).subscribe(r => {
          this.cluster = r;
        })
        this.prepareDownload();
      }
    });
  }

  prepareDownload() {
    this.s3Err = null;
    this.uiLocalClusterControllerService.getVagrantUsingGET(this.clusterId).subscribe(
      x => {
        this.s3Url = x;
      },
      error =>{

        this.s3Err = "Something went wrong, Download is not ready: " + JSON.parse(error.error).message || error.message;
      }
    );
  }
}
