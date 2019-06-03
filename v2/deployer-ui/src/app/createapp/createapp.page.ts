import { Component, OnInit, Input } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { Application } from '../api/models';
import { LoadingController, NavController, ModalController } from '@ionic/angular';

@Component({
  selector: 'app-createapp',
  templateUrl: './createapp.page.html',
  styleUrls: ['./createapp.page.scss'],
})
export class CreateappPage implements OnInit {

  @Input() application: Application = {ports: []};

  @Input() applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

  constructor(private applicationControllerService: ApplicationControllerService,
    private activatedRoute: ActivatedRoute, private loadingController: LoadingController,
    private navController: NavController, private modalController: ModalController) {
  }

  ngOnInit() {
  }

  createApplication() {
    this.loadingController.create({
      message: 'Creating...',
      duration: 60000
    }).then((res) => {
      res.present();
      this.application.applicationFamily = this.applicationFamily;
      this.applicationControllerService.createApplicationUsingPOST({application: this.application, applicationFamily: this.applicationFamily})
      .subscribe((app: Application) => {
        this.modalController.dismiss();
        this.navController.navigateForward(`/${this.applicationFamily}/applications/${app.id}`);
        res.remove();
      }, err => {
        console.log(err);
      })
    });
  }

  addPort() {
    this.application.ports.push({});
  }
}
