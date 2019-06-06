import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../api/services';
import { ActivatedRoute } from '@angular/router';
import { DeploymentStatusDetails } from '../api/models';

@Component({
  selector: 'app-deploymentstatus',
  templateUrl: './deploymentstatus.page.html',
  styleUrls: ['./deploymentstatus.page.scss'],
})
export class DeploymentstatusPage implements OnInit {
  deploymentStatus: DeploymentStatusDetails;

  constructor(private applicationControllerService: ApplicationControllerService, private activatedRoute: ActivatedRoute) { }


  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        var applicationFamily:  'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS' = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'> params.get("applicationFamily");
        var applicationId: string = params.get("applicationId");
        var environemnt: string = params.get("environment");
        this.applicationControllerService.getDeploymentStatusUsingGET(
          {
            applicationFamily: applicationFamily,
            applicationId: applicationId,
            environment: environemnt
          }
        ).subscribe(deploymentStatus => this.deploymentStatus = deploymentStatus);
      }
    );
  }

}
