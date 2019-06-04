import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { NavController } from '@ionic/angular';
import { Build, LogEvent, TokenPaginatedResponseLogEvent } from '../api/models';
import { timer } from 'rxjs';

@Component({
  selector: 'app-buildstatus',
  templateUrl: './buildstatus.page.html',
  styleUrls: ['./buildstatus.page.scss'],
})
export class BuildstatusPage implements OnInit {

  build: Build;
  logs: TokenPaginatedResponseLogEvent;
  applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationId: string;
  buildId: string;
  subscription: any;

  constructor(private applicationControllerService: ApplicationControllerService, private activatedRoute: ActivatedRoute,
    private navController: NavController) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        this.applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        this.applicationId = params.get("applicationId");
        this.buildId = params.get("buildId");
        this.subscription = timer(0, 1000).subscribe(() => this.loadBuild());
      }
    );
  }

  loadBuild() {
    this.applicationControllerService.getBuildUsingGET({
      applicationFamily: this.applicationFamily,
      applicationId: this.applicationId,
      buildId: this.buildId
    })
    .subscribe(
      (build: Build) => {
        this.build = build;
        if (this.build.status === 'SUCCEEDED' || this.build.status === 'FAILED') {
          this.subscription.unsubscribe();
        }
      },
      err => {console.log(err); this.navController.navigateForward("/signin");
    });
    this.applicationControllerService.getBuildLogsUsingGET({
      applicationFamily: this.applicationFamily,
      applicationId: this.applicationId,
      buildId: this.buildId
    }).subscribe(
      (logs: TokenPaginatedResponseLogEvent) => this.logs = logs,
      err => {console.log(err); this.navController.navigateForward("/signin");
    });
  }

  deploy() {

  }

}
