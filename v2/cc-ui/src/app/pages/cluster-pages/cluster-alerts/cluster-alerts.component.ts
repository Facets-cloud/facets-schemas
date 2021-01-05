import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UiCommonClusterControllerService} from "../../../cc-api/services/ui-common-cluster-controller.service";
import {NbDialogService} from "@nebular/theme";
import {SilenceAlarmRequest} from "../../../cc-api/models";
import { FormControl } from '@angular/forms';


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
  comments: string = "";
  hours: number;
  hoursEmpty: boolean = false;
  commentsEmpty: boolean = false;


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
    this.commentsEmpty = false;
    this.hoursEmpty= false;
    this.dialogService.open(silenceInput, {context: alert});
  }

  silenceAlertPost(data: any, ref: any) {
    if(!this.comments){
      this.commentsEmpty= true;
      return;
    }else{
      this.commentsEmpty = false;
    }
    if(!this.hours || this.hours == 0 ){
      this.hoursEmpty= true;
      return;
    }else{
      this.hoursEmpty= false;
    }
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

  changed() {
    this.commentsEmpty = !this.comments;
    this.hoursEmpty = !this.hours || this.hours == 0;
  }
}
