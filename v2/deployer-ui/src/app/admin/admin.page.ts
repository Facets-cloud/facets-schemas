import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.page.html',
  styleUrls: ['./admin.page.scss'],
})
export class AdminPage implements OnInit {

  constructor(private applicationControllerService: ApplicationControllerService, private navController: NavController) {
  }

  applicationFamilies: ("CRM" | "ECOMMERCE" | "INTEGRATIONS" | "OPS")[];

  ngOnInit() {
    this.applicationControllerService.getApplicationFamiliesUsingGET()
      .subscribe(applicationFamily => this.applicationFamilies = applicationFamily,
        err => {console.log(err); this.navController.navigateForward("/signin");});
  }


}
