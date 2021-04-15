
import { Component, Input, OnInit } from '@angular/core';

import { ViewCell } from 'ng2-smart-table';

@Component({
  template: `
    <nb-user [name]=renderValue [picture]="rowData.picture" [showTitle]="false"></nb-user>
  `,
})
export class UserRendererComponent implements ViewCell, OnInit {

  renderValue: string;

  @Input() value: string | number;
  @Input() rowData: any;

  ngOnInit() {
    this.renderValue = this.value.toString();
  }

}
