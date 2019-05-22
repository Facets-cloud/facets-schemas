import { Component, OnInit } from '@angular/core';
import { Application, Build } from '../api/models';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-build',
  templateUrl: './build.page.html',
  styleUrls: ['./build.page.scss'],
})
export class BuildPage implements OnInit {

  application: Application;
  build: Build = {}

  constructor(private applicationControllerService: ApplicationControllerService, private activatedRoute: ActivatedRoute,
    private navController: NavController) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        var applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        var applicationId: string = params.get("applicationId");
        this.applicationControllerService.getApplicationUsingGET({applicationFamily: applicationFamily,
          applicationId: applicationId})
        .subscribe(application => this.application = application,
          err => {console.log(err); this.navController.navigateForward("/signin");
        });
      }
    );
  }

  startBuild() {
    this.build.applicationId = this.application.id
    this.build.environmentVariable = {}
    this.applicationControllerService.buildUsingPOST({
      applicationId: this.application.id,
      applicationFamily: this.application.applicationFamily,
      build: this.build
    }).subscribe(
      (build: Build) =>
        this.navController.navigateForward(`/${this.application.applicationFamily}/applications/${this.application.id}/builds/${build.id}`),
      err => {console.log(err); this.navController.navigateForward("/signin");}
    );
  }


}
