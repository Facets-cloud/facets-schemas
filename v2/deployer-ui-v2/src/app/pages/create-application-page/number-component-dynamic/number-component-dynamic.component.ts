import { Component, OnInit } from '@angular/core';
import { DefaultEditor } from 'ng2-smart-table';

@Component({
  selector: 'number-component-dynamic',
  templateUrl: './number-component-dynamic.component.html',
  styleUrls: ['./number-component-dynamic.component.scss']
})
export class NumberComponentDynamicComponent extends DefaultEditor implements OnInit {

  ngOnInit() {
    this.cell.newValue = this.cell.getValue();
  }

}
