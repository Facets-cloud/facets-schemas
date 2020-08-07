import { Component, OnInit, Input, OnChanges} from '@angular/core';
import { Application, ApplicationMetricsWrapper } from '../../api/models';
import { ApplicationControllerService } from '../../api/services';
import {ActivatedRoute} from "@angular/router";

@Component({
selector: 'application-metrics-all',
templateUrl: './application-metrics-all.component.html',
styleUrls: ['./application-metrics-all.component.scss']
})

export class ApplicationMetricsAllComponent implements OnInit, OnChanges {

  // @Input()
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  appFamilies: Array<"CRM" | "ECOMMERCE" | "INTEGRATIONS" | "OPS">;
  applicationMetricsWrappers: ApplicationMetricsWrapper [] = [];
  ngOnChanges(changes: import("@angular/core").SimpleChanges): void {
    this.ngOnInit();
  }


  constructor(private applicationControllerService: ApplicationControllerService,
              private activatedRoute: ActivatedRoute) { }


  populateAppFamilies() {
    this.applicationControllerService.getApplicationFamiliesUsingGET().subscribe(
      appFamilies => {
        this.appFamilies = appFamilies;
      },
    );
  }
  ngOnInit() {
    // this.activatedRoute.paramMap.subscribe(
    //   paramMap => {
    //     this.applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>paramMap.get('appFamily');
    //   });

    this.populateAppFamilies();

  }
  loadMetrics() {
    this.applicationControllerService.getAllApplicationMetricsUsingGETResponse(this.applicationFamily)
      .subscribe(resp => {

        this.applicationMetricsWrappers = resp.body;

        this.applicationFamily = this.appFamilies[0];

      });
  }
}




