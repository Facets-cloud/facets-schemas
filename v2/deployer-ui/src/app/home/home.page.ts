import { Component } from '@angular/core';
import { NavController } from '@ionic/angular';
import { ApplicationControllerService } from '../api/services';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  constructor(private applicationControllerService: ApplicationControllerService, private navController: NavController) {
  }

  applicationFamilies: ("CRM" | "ECOMMERCE" | "INTEGRATIONS" | "OPS")[];

  ngOnInit() {
    this.applicationControllerService.getApplicationFamiliesUsingGET()
      .subscribe(applicationFamily => this.applicationFamilies = applicationFamily,
        err => {console.log(err); this.navController.navigateForward("/signin");});
  }
}
