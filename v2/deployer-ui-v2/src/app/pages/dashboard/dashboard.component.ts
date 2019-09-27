import { Component } from '@angular/core';
import { ApplicationControllerService } from '../../api/services';
import { GlobalStats } from '../../api/models';

@Component({
  selector: 'ngx-dashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  stats: GlobalStats;

  constructor(private applicationControllerService: ApplicationControllerService) {
    this.applicationControllerService.globalStatsUsingGET().subscribe(stats => this.stats = stats);
  }
}
