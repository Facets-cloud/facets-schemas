import { Component, OnInit, Input, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { Build, LogEvent } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';
import { timer, Subscription } from 'rxjs';

@Component({
  selector: 'build-logs',
  templateUrl: './build-logs.component.html',
  styleUrls: ['./build-logs.component.scss']
})
export class BuildLogsComponent implements OnInit, OnDestroy, OnChanges {

  subscription: Subscription;

  @Input() build: Build;

  logs: Array<LogEvent> = new Array<LogEvent>();

  constructor(private applicationControllerService: ApplicationControllerService) { }

  paused: boolean = false;

  ngOnChanges(changes: SimpleChanges): void {
    this.subscribeToLogs();
  }

  ngOnInit() {
    this.subscribeToLogs();
  }

  private subscribeToLogs() {
    if (this.build.status === 'IN_PROGRESS') {
      this.subscription = timer(0, 1000).subscribe(() => this.refresh());
    }

    if (this.build.status !== undefined) {
      this.refresh();
    }
  }

  refresh() {
    this.applicationControllerService.getBuildLogsUsingGET({
      buildId: this.build.id,
      applicationFamily: this.build.applicationFamily,
      applicationId: this.build.applicationId,
    }).subscribe(x => this.logs = x.logEventList.reverse());
  }

  pause() {
    this.paused = true;
    this.subscription.unsubscribe();
  }

  resume() {
    this.paused = false;
    this.subscription = timer(0, 1000).subscribe(() => this.refresh());
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
