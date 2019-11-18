import { Component, OnInit, Input } from '@angular/core';
import { ViewCell } from 'ng2-smart-table';
import { Application } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'button-download-component',
  template: `
  <a href="{{ getLink() }}">
  <button nbTooltip="Download File" nbButton appearance="ghost"><nb-icon pack="eva" icon="download-outline"></nb-icon></button>
  </a>
  `,
})
export class ButtonDownloadComponent implements OnInit {

  @Input() rowData: any;

  constructor() {  }

  ngOnInit() {
  }

  getLink(): string {
    const application: Application = this.rowData.application;
    const appFamily = application.applicationFamily;
    const env = this.rowData.environment;
    const downloadPath = appFamily + '/'
                        + env + '/'
                        + application.name + '/'
                        + this.rowData.date + 'H'
                        + this.rowData.hour + '/'
                        + this.rowData.name;
    const completeUrl = `/api/${appFamily}/${env}/applications/${application.id}/dumps/download?path=${downloadPath}`;
    return completeUrl;
  }

}
