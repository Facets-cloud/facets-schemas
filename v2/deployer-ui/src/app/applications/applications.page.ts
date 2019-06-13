import { Component, OnInit } from '@angular/core';
import { Application } from '../api/models';
import { ApplicationControllerService } from '../api/services';
import { AppRoutingModule } from '../app-routing.module';
import { ActivatedRoute } from '@angular/router';
import { NavController, ModalController } from '@ionic/angular';
import { CreateappPage } from '../createapp/createapp.page';

@Component({
  selector: 'app-applications',
  templateUrl: './applications.page.html',
  styleUrls: ['./applications.page.scss'],
})
export class ApplicationsPage implements OnInit {

  applications: Application[];
  applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

  constructor(private applicationControllerService: ApplicationControllerService,
    private activatedRoute: ActivatedRoute, private navController: NavController,
    private modalController: ModalController) {
  }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        this.applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        this.applicationControllerService.getApplicationsUsingGET(this.applicationFamily)
      .subscribe(applications => this.applications = applications,
        err => {console.log(err); this.navController.navigateForward("/signin");});
    });
  }

}
