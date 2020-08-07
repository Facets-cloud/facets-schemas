import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { Application, ApplicationMetrics } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';

@Component({
selector: 'application-metrics',
templateUrl: './application-metrics.component.html',
styleUrls: ['./application-metrics.component.scss']
})

export class ApplicationMetricsComponent implements OnInit, OnChanges {

  ngOnChanges(changes: import("@angular/core").SimpleChanges): void {
    this.ngOnInit();
  }

  @Input() application: Application;

   oldMetrics: ApplicationMetrics = null;
   newMetrics: ApplicationMetrics = null;

  constructor(private applicationControllerService: ApplicationControllerService) { }

  ngOnInit() {
    if (!this.application || !this.application.id) {
      return;
    }

    this.applicationControllerService.getApplicationMetricSummaryUsingGET({
      applicationFamily: this.application.applicationFamily,
      applicationId: this.application.id,
    }).subscribe(body => {

    this.oldMetrics = body.old;
    this.newMetrics = body.new;
    });
  }

}




