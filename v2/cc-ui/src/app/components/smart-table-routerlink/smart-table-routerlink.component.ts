import { Component, Input, OnInit } from '@angular/core';

import { ViewCell } from 'ng2-smart-table';

@Component({
  template: `
    <a [routerLink]="renderValue.url">{{renderValue.display}}</a>
  `,
})
export class SmartTableRouterlink implements ViewCell, OnInit {

  @Input() value: any;
  @Input() rowData: any;
  renderValue: { display: string; url: [] };

  ngOnInit() {
    this.renderValue = JSON.parse(this.value);
  }

}
