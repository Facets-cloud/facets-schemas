import { Component, OnInit, Input } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { Application } from '../api/models';
import { LoadingController, NavController, ModalController } from '@ionic/angular';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-createapp',
  templateUrl: './createapp.page.html',
  styleUrls: ['./createapp.page.scss'],
})
export class CreateappPage implements OnInit {

  @Input() enableCifsMount: boolean;
  @Input() application: Application = {
    ports: [],
    healthCheck: {
      livenessProbe: {},
      readinessProbe: {}
    },
  };

  @Input() applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

  constructor(private applicationControllerService: ApplicationControllerService,
    private activatedRoute: ActivatedRoute, private loadingController: LoadingController,
    private navController: NavController, private modalController: ModalController) {
  }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        this.applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        if(params.has("applicationId")) {
          this.applicationControllerService.getApplicationUsingGET({
            applicationFamily: this.applicationFamily,
            applicationId: params.get("applicationId")
          }).subscribe(
            app => {
              this.application = app;
              if (!this.application.healthCheck) {
                this.application.healthCheck = {
                  livenessProbe: {},
                  readinessProbe: {}
                };
              }
              if (app.additionalParams['mountCifs']) {
                this.enableCifsMount = app.additionalParams["mountCifs"] === "true";
              }
            }
          );
        }
      }
    );
  }

  createApplication() {
    this.loadingController.create({
      message: 'Creating...',
      duration: 60000
    }).then((res) => {
      res.present();
      this.application.additionalParams = this.enableCifsMount ? {"mountCifs": "true"} : {};
      this.application.dnsType = this.application.loadBalancerType === 'INTERNAL' ? 'PRIVATE' : 'PUBLIC';
      this.application.applicationFamily = this.applicationFamily;
      let response: Observable<Application> = null;
      if(!this.application.id) {
        response = this.applicationControllerService.createApplicationUsingPOST({application: this.application,
          applicationFamily: this.applicationFamily});
      } else {
        response = this.applicationControllerService.updateApplicationUsingPUT({application: this.application,
          applicationFamily: this.applicationFamily});
      }
      response.subscribe((app: Application) => {
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
