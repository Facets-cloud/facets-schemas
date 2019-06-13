import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { NavController, PopoverController, ModalController } from '@ionic/angular';
import { Build, LogEvent, TokenPaginatedResponseLogEvent } from '../api/models';
import { timer, Observable } from 'rxjs';
import { AppMenuPage } from '../app-menu/app-menu.page';
import { ConfirmationDialogPage } from '../confirmation-dialog/confirmation-dialog.page';

@Component({
  selector: 'app-buildstatus',
  templateUrl: './buildstatus.page.html',
  styleUrls: ['./buildstatus.page.scss'],
})
export class BuildstatusPage implements OnInit {

  build: Build;
  applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationId: string;
  buildId: string;
  subscription: any;
  logs: LogEvent[];

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
      async (build: Build) => {
        this.build = build;
        await this.getLogs();
        if (this.build.status === 'SUCCEEDED' || this.build.status === 'FAILED') {
          this.subscription.unsubscribe();
        }
      },
      err => {console.log(err); this.navController.navigateForward("/signin");
    });
  }

  // private getLogs() {
  //   this.applicationControllerService.getBuildLogsUsingGET({
  //     applicationFamily: this.applicationFamily,
  //     applicationId: this.applicationId,
  //     buildId: this.buildId,
  //     nextToken: this.logTokens[this.logTokens.length - 1]
  //   }).subscribe((logs: TokenPaginatedResponseLogEvent) => {
  //     this.logsMap[this.logTokens[this.logTokens.length - 1]] = logs.logEventList;
  //     if (logs.nextToken === this.logTokens[this.logTokens.length - 1]) {
  //       console.log("Logs so far fetched");
  //     }
  //     else {
  //       this.logTokens.push(logs.nextToken);
  //       this.getLogs();
  //     }
  //   }, err => {
  //     console.log(err);
  //     this.navController.navigateForward("/signin");
  //   });
  // }

  async getLogs() {
    this.logs = (await this.applicationControllerService.getBuildLogsUsingGET(
      {
        applicationFamily: this.applicationFamily,
        applicationId: this.applicationId,
        buildId: this.buildId
      }).toPromise()).logEventList;
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
