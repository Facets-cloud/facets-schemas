import { Component, OnInit, Input } from '@angular/core';
import { Application } from '../../../api/models';

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

    return `/api/${appFamily}/${env}/applications/${application.id}/dumps/download?path=${this.rowData.path}`;
  }

}
