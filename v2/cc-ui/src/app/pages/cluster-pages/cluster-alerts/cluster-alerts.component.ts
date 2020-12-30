import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UiCommonClusterControllerService} from "../../../cc-api/services/ui-common-cluster-controller.service";
import {NbDialogService} from "@nebular/theme";
import {SilenceAlarmRequest} from "../../../cc-api/models";

@Component({
  selector: 'app-cluster-alerts',
  templateUrl: './cluster-alerts.component.html',
  styleUrls: ['./cluster-alerts.component.scss']
})
export class ClusterAlertsComponent implements OnInit {
  private stackName: any;
  isReachable: boolean = false;
  rules: any = [];
  reason: any;
  loading: boolean = true;
  private clusterId: any;

  constructor(private aWSClusterService: UiCommonClusterControllerService,
              private route: ActivatedRoute,
              private dialogService: NbDialogService
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        this.clusterId = p.clusterId;
        this.aWSClusterService.getAlertsUsingGET(this.clusterId).subscribe(t => {
          if (t["status"] === "success") {
            this.isReachable = true;
            this.rules = t["data"]["groups"][0]["rules"];
          } else {
            this.reason = t["reason"];
          }
          this.loading = false;
        });
      }
      this.stackName = p.stackName;
    });

  }

  showDetails(dialog: any, alert: any) {
    this.dialogService.open(dialog, {context: alert});
  }

  silenceAlert(silenceInput: any, alert: any) {
    this.dialogService.open(silenceInput, {context: alert});
  }

  silenceAlertPost(data: any, ref: any) {
    let request: SilenceAlarmRequest = {
      comment: "Hardcoded",
      snoozeInHours: 2,
      labels: data["labels"]
    };
    this.aWSClusterService.silenceAlertsUsingPOST({
      request: request,
      clusterId: this.clusterId
    }).subscribe(result => {
      console.log('Silenced Alert');
      this.loading=true;
      this.isReachable=false;
      this.ngOnInit();
      ref.close();
    });
  }

}
