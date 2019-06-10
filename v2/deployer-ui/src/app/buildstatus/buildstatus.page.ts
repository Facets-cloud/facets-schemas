import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { NavController, PopoverController, ModalController } from '@ionic/angular';
import { Build, LogEvent, TokenPaginatedResponseLogEvent } from '../api/models';
import { timer } from 'rxjs';
import { AppMenuPage } from '../app-menu/app-menu.page';
import { ConfirmationDialogPage } from '../confirmation-dialog/confirmation-dialog.page';

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
    private navController: NavController, private popoverController: PopoverController, private modalController: ModalController) { }

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
      (logs: TokenPaginatedResponseLogEvent) => {
        if(! this.logs) {
          this.logs = logs;
        }
        logs.logEventList.forEach(e => {
          this.logs.logEventList.push(e);
        });
      },
      err => {console.log(err); this.navController.navigateForward("/signin");
    });
  }

  async presentPopover(ev) {
    const menuItems = [];

    if (this.build.status === 'SUCCEEDED') {
      const deployMenuItem = this.getDeploymentMenuItem();
      menuItems.push(deployMenuItem);
    }

    if (!this.build.promoted) {
      const promotionMenuItem = await this.getPrmotionMenuItem();
      menuItems.push(promotionMenuItem);
    }

    const popover = await this.popoverController.create({
      component: AppMenuPage,
      componentProps: {
        menuItems: menuItems
      },
      event: ev,
      translucent: true
    });
    return await popover.present();
  }


  private async getPrmotionMenuItem(): Promise<any> {
    const promotionModal = await this.getPromotionModal();
    return {
      name: "Promote",
      icon: "star",
      modal: promotionModal
    };
  }

  private getDeploymentMenuItem() {
    return {
      name: "Deploy",
      icon: "paper-plane",
      url: `/${this.applicationFamily}/applications/${this.applicationId}/builds/${this.buildId}/deploy`
    };
  }

  private async getPromotionModal() {
    return await this.modalController.create({
      component: ConfirmationDialogPage,
      componentProps: {
        title: "Confirm Promotion",
        infoText: "Promoting this build will make it available for deployment in production environment(s).",
        callback: this.promote()
      }
    });
  }

  private promote(): any {
    return () => {
      this.build.promoted = true;
      this.applicationControllerService.updateBuildUsingPUT({
        applicationFamily: this.applicationFamily,
        applicationId: this.applicationId,
        buildId: this.buildId,
        build: this.build
      }).subscribe(() => this.modalController.dismiss());
    };
  }
}
