import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { Application } from '../api/models';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-appdetails',
  templateUrl: './appdetails.page.html',
  styleUrls: ['./appdetails.page.scss'],
})
export class AppdetailsPage implements OnInit {

  application: Application;

  constructor(private applicationControllerService: ApplicationControllerService, private activatedRoute: ActivatedRoute,
    private navController: NavController) { }

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

}
