import { Component, OnInit, Input } from '@angular/core';
import { Application, Build } from '../api/models';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { NavController, ModalController } from '@ionic/angular';

@Component({
  selector: 'app-build',
  templateUrl: './build.page.html',
  styleUrls: ['./build.page.scss'],
})
export class BuildPage implements OnInit {

  @Input() application: Application;
  build: Build = {}

  constructor(private applicationControllerService: ApplicationControllerService, private activatedRoute: ActivatedRoute,
    private navController: NavController, private modalController: ModalController) { }

  ngOnInit() {
  }

  startBuild() {
    this.build.applicationId = this.application.id
    this.build.environmentVariables = {}
    this.applicationControllerService.buildUsingPOST({
      applicationId: this.application.id,
      applicationFamily: this.application.applicationFamily,
      build: this.build
    }).subscribe(
      (build: Build) => {
        this.modalController.dismiss();
        this.navController.navigateForward(`/${this.application.applicationFamily}/applications/${this.application.id}/builds/${build.id}`),
      err => {console.log(err); this.navController.navigateForward("/signin");}
      }
    );
  }


}
