import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { Application, EnvironmentMetaData, Alerting } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';

@Component({
  selector: 'alerting',
  templateUrl: './alerting.component.html',
  styleUrls: ['./alerting.component.scss']
})
export class AlertingComponent implements OnInit, OnChanges {

  ngOnChanges(changes: import("@angular/core").SimpleChanges): void {
    this.ngOnInit();
  }

  @Input() application: Application;
  environments: EnvironmentMetaData[];
  alertingDetails: Alerting[];
  settings: any;
  loading: boolean = false;

  private async enableDisableAlerting(event) {
    this.loading = true;
    if (event.enabled) {
      this.applicationControllerService.enableAlertingUsingPOST({
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
        environment: event.environment,
      }).subscribe(async x => {
        await this.getAlertingDetails();
      });
    } else {
      this.applicationControllerService.disableAlertingUsingDELETE({
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
        environment: event.environment,
      }).subscribe(async x => {
        await this.getAlertingDetails();
      });
    }
  }

  getSettings() {
    return {
      columns: {
        environmentName: {
          title: 'Cluster',
        },
        newRelicAlertsUrl: {
          title: 'NewRelic Alerts',
          type: 'custom',
          renderComponent: NewRelicAlertLinkViewComponent,
        },
        custom: {
          title: 'Enable/Disable',
          type: 'custom',
          renderComponent: AlertingEnableButtonComponent,
          onComponentInitFunction: (instance: AlertingEnableButtonComponent) => {
            instance.alertingToggle.subscribe(e => {
              this.enableDisableAlerting(e);
            });
          },
        },
      },
      actions: false,
      hideSubHeader: true,
    };
  }

  constructor(private applicationControllerService: ApplicationControllerService) { }

  async ngOnInit() {
    if (!this.application || !this.application.id) {
      return;
    }
    await this.getAlertingDetails();
  }


  private async getAlertingDetails() {
    const alertingDetails = [];
    this.environments =
      await this.applicationControllerService.getEnvironmentMetaDataUsingGET(this.application.applicationFamily).toPromise();
    this.settings = this.getSettings();
    for (const env of this.environments) {
      const alerting = await this.applicationControllerService.getAlertingDetailsUsingGET({
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
        environment: env.name,
      }).toPromise();
      alertingDetails.push(alerting);
    }
    this.alertingDetails = alertingDetails;
    this.loading = false;
  }
}


//NewRelicLinkViewComponent
@Component({
  selector: 'newrelic-alerts-link-view',
  template: `<p *ngIf="!rowData?.newRelicAlertsUrl">Enable Alerting</p><a target="_blank" *ngIf="rowData?.newRelicAlertsUrl" href="{{rowData.newRelicAlertsUrl}}">Open Alert Policy</a>`,
})
export class NewRelicAlertLinkViewComponent {
  @Input() rowData: Alerting;
}


@Component({
  selector: 'newrelic-alerts-link-view',
  template: `<nb-toggle [(checked)]="enabled" (checkedChange)="changed()"></nb-toggle>`,
})
export class AlertingEnableButtonComponent implements OnInit {
  ngOnInit(): void {
    if (this.rowData.newRelicAlertsUrl) {
      this.enabled = true;
    }
  }
  @Input() rowData: Alerting;
  @Output() alertingToggle = new EventEmitter<any>();
  enabled: boolean = false;
  changed() {
    this.alertingToggle.emit({'environment' : this.rowData.environmentName, 'enabled' : this.enabled});
  }
}
