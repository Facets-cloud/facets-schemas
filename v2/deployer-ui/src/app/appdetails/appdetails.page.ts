import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { Application } from '../api/models';
import { NavController, MenuController, ModalController } from '@ionic/angular';
import { BuildPage } from '../build/build.page';

@Component({
  selector: 'app-appdetails',
  templateUrl: './appdetails.page.html',
  styleUrls: ['./appdetails.page.scss'],
})
export class AppdetailsPage implements OnInit {

  application: Application;

  constructor(private applicationControllerService: ApplicationControllerService, private activatedRoute: ActivatedRoute,
    private navController: NavController, private modalController: ModalController) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        var applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        var applicationId: string = params.get("applicationId");
        this.applicationControllerService.getApplicationUsingGET({applicationFamily: applicationFamily,
          applicationId: applicationId})
        .subscribe(application => this.application = application,err => {console.log(err); this.navController.navigateForward("/signin");});
      }
    );
  }

  async presentModal() {
    const modal = await this.modalController.create(
      {
        component: BuildPage,
        componentProps: {
          application: this.application
        }
      }
    );
    return await modal.present();
  }

  listBuilds() {
    this.navController.navigateForward(`/${this.application.applicationFamily}/applications/${this.application.id}/builds`);
  }

}
