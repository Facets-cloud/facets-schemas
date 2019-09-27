import { Component, OnInit, Input } from '@angular/core';
import { Application } from '../../../api/models';
import { Router } from '@angular/router';

@Component({
  selector: 'app-info',
  templateUrl: './app-info.component.html',
  styleUrls: ['./app-info.component.scss']
})
export class AppInfoComponent implements OnInit {

  @Input() application: Application;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  updateApplication() {
    this.router.navigate(['pages', 'applications', this.application.applicationFamily, this.application.id, 'edit']);
  }

}
