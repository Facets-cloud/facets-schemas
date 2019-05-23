import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { NavController } from '@ionic/angular';
import { Build, LogEvent } from '../api/models';

@Component({
  selector: 'app-buildstatus',
  templateUrl: './buildstatus.page.html',
  styleUrls: ['./buildstatus.page.scss'],
})
export class BuildstatusPage implements OnInit {

  build: Build;
  logs: LogEvent[] = [];

  constructor(private applicationControllerService: ApplicationControllerService, private activatedRoute: ActivatedRoute,
    private navController: NavController) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        var applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        var applicationId: string = params.get("applicationId");
        var buildId: string = params.get("buildId");
        this.applicationControllerService.getBuildUsingGET({
          applicationFamily: applicationFamily,
          applicationId: applicationId,
          buildId: buildId
        })
        .subscribe(
          (build: Build) => this.build = build,
          err => {console.log(err); this.navController.navigateForward("/signin");
        });
        this.applicationControllerService.getBuildLogsUsingGET({
          applicationFamily: applicationFamily,
          applicationId: applicationId,
          buildId: buildId
        }).subscribe(
          (logs: LogEvent[]) => this.logs = logs,
          err => {console.log(err); this.navController.navigateForward("/signin");
        });
      }
    );
  }

}
