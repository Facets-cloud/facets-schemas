import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { Application } from '../api/models';
import { NavController, MenuController, ModalController, PopoverController } from '@ionic/angular';
import { BuildPage } from '../build/build.page';
import { AppMenuPage } from '../app-menu/app-menu.page';

@Component({
  selector: 'app-appdetails',
  templateUrl: './appdetails.page.html',
  styleUrls: ['./appdetails.page.scss'],
})
export class AppdetailsPage implements OnInit {

  application: Application;

  constructor(private applicationControllerService: ApplicationControllerService, private activatedRoute: ActivatedRoute,
    private navController: NavController, private modalController: ModalController,
    private popoverController: PopoverController) { }

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

  async presentPopover(ev) {
    const popover = await this.popoverController.create({
      component: AppMenuPage,
      componentProps: {
        menuItems: [
          {
            name: "Build",
            icon: "construct",
            modal: await this.modalController.create(
              {
                component: BuildPage,
                componentProps: {
                  application: this.application
                }
              }
            )
          },
          {
            name: "List Builds",
            icon: "list",
            url: `/${this.application.applicationFamily}/applications/${this.application.id}/builds`
          },
          {
            name: "Update Application",
            icon: "create",
            url: `/${this.application.applicationFamily}/applications/${this.application.id}/update`
          },
          {
            name: "Current Deployments",
            icon: "apps",
            url: `/${this.application.applicationFamily}/applications/${this.application.id}/currentdeployments`
          }
        ]
      },
      event: ev,
      translucent: true
    });
    return await popover.present();
  }

}
