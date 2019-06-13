import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApplicationControllerService } from '../api/services';
import { Application, Build } from '../api/models';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-build-list',
  templateUrl: './build-list.page.html',
  styleUrls: ['./build-list.page.scss'],
})
export class BuildListPage implements OnInit {
  builds: Build[];
  applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  applicationId: string;

  constructor(private activatedRoute: ActivatedRoute,
    private applicationControllerService: ApplicationControllerService,
    private navController: NavController) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        this.applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        this.applicationId = params.get("applicationId");
        this.applicationControllerService.getBuildsUsingGET({applicationFamily: this.applicationFamily,
          applicationId: this.applicationId})
        .subscribe(builds => this.builds = builds,
          err => {console.log(err); this.navController.navigateForward("/signin");});
      }
    );
  }
}
