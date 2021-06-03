import { Component, OnInit } from '@angular/core';
import {UiAwsClusterControllerService} from "../../../cc-api/services/ui-aws-cluster-controller.service";
import {UiCommonClusterControllerService} from "../../../cc-api/services/ui-common-cluster-controller.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NbDialogService, NbToastrService} from "@nebular/theme";
import {UiStackControllerService} from "../../../cc-api/services/ui-stack-controller.service";
import {ApplicationControllerService} from "../../../cc-api/services/application-controller.service";

@Component({
  selector: 'app-cluster-overview-assembler',
  templateUrl: './cluster-overview-assembler.component.html',
  styleUrls: ['./cluster-overview-assembler.component.scss']
})
export class ClusterOverviewAssemblerComponent implements OnInit {
  cloud: "AWS" | "AZURE" | "LOCAL";

  constructor(
              private uiCommonClusterControllerService: UiCommonClusterControllerService,
              private route: ActivatedRoute,) { }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        this.uiCommonClusterControllerService.getClusterCommonUsingGET(p.clusterId).subscribe(
          r => {
            this.cloud = r.cloud;
          }
        )
      }
    });
  }

}
