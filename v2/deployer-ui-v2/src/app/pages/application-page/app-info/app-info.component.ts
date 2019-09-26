import { Component, OnInit, Input } from '@angular/core';
import { Application } from '../../../api/models';

@Component({
  selector: 'app-info',
  templateUrl: './app-info.component.html',
  styleUrls: ['./app-info.component.scss']
})
export class AppInfoComponent implements OnInit {

  @Input() application: Application;

  constructor() { }

  ngOnInit() {
  }

}
