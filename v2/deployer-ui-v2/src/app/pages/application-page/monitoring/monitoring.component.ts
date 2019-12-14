import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { Application, EnvironmentMetaData, Monitoring } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';

@Component({
  selector: 'monitoring',
  templateUrl: './monitoring.component.html',
  styleUrls: ['./monitoring.component.scss']
})
export class MonitoringComponent implements OnInit, OnChanges {

  ngOnChanges(changes: import("@angular/core").SimpleChanges): void {
    this.ngOnInit();
  }

  @Input() application: Application;
  environments: EnvironmentMetaData[];
  monitoringDetails: Monitoring[];
  settings: any;
  loading: boolean = false;

  private async enableDisableMonitoring(event) {
    this.loading = true;
    if (event.enabled) {
      this.applicationControllerService.enableMonitoringUsingPOST({
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
        environment: event.environment,
      }).subscribe(async x => {
        await this.getMonitoringDetails();
      });
    } else {
      this.applicationControllerService.disableMonitoringUsingDELETE({
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
        environment: event.environment,
      }).subscribe(async x => {
        await this.getMonitoringDetails();
      });
    }
  }

  getSettings() {
    return {
      columns: {
        environmentName: {
          title: 'Cluster',
        },
        newRelicDashboardUrl: {
          title: 'NewRelic Dashboard',
          type: 'custom',
          renderComponent: NewRelicLinkViewComponent,
        },
        custom: {
          title: 'Enable/Disable',
          type: 'custom',
          renderComponent: MonitoringEnableButtonComponent,
          onComponentInitFunction: (instance: MonitoringEnableButtonComponent) => {
            instance.monitoringToggle.subscribe(e => {
              this.enableDisableMonitoring(e);
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
    await this.getMonitoringDetails();
  }


  private async getMonitoringDetails() {
    const monitoringDetails = [];
    this.environments =
      await this.applicationControllerService.getEnvironmentMetaDataUsingGET(this.application.applicationFamily).toPromise();
    this.settings = this.getSettings();
    for (const env of this.environments) {
      const monitoring = await this.applicationControllerService.getMonitoringDetailsUsingGET({
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
        environment: env.name,
      }).toPromise();
      monitoringDetails.push(monitoring);
    }
    this.monitoringDetails = monitoringDetails;
    this.loading = false;
  }
}

// NewRelicLinkViewComponent
@Component({
  selector: 'newrelic-link-view',
  template: `<p *ngIf="!rowData?.newRelicDashboardUrl">Enable Monitoring to create Dashboard</p><a target="_blank" *ngIf="rowData?.newRelicDashboardUrl" href="{{rowData.newRelicDashboardUrl}}">Open</a>`,
})
export class NewRelicLinkViewComponent {
  @Input() rowData: Monitoring;
}

// MonitoringEnableButtonComponent
@Component({
  selector: 'newrelic-link-view',
  template: `<nb-toggle [(checked)]="enabled" (checkedChange)="changed()"></nb-toggle>`,
})
export class MonitoringEnableButtonComponent implements OnInit {
  ngOnInit(): void {
    if (this.rowData.newRelicDashboardUrl) {
      this.enabled = true;
    }
  }
  @Input() rowData: Monitoring;
  @Output() monitoringToggle = new EventEmitter<any>();
  enabled: boolean = false;
  changed() {
    this.monitoringToggle.emit({'environment' : this.rowData.environmentName, 'enabled' : this.enabled});
  }
}

