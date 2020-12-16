/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from './@core/utils/analytics.service';
import { ApplicationControllerService } from './api/services';
import { SimpleOauth2User } from './api/models';
import { Router } from '@angular/router';

@Component({
  selector: 'ngx-app',
  template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {

  constructor(private analytics: AnalyticsService,
    private applicationControllerService: ApplicationControllerService, private router: Router) {
  }

  ngOnInit() {
    this.applicationControllerService.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {},
      (error) => {
        console.log(error);
        this.router.navigate(['/pages/signin']);
      }
    );
    this.analytics.trackPageViews();
  }
}
