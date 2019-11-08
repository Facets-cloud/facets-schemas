import { Component, OnInit, TemplateRef, Input, OnChanges, SimpleChanges, HostListener } from '@angular/core';
import { Build, Application } from '../../../api/models';
import { NbDialogService, NbDialogRef } from '@nebular/theme';
import { ApplicationControllerService } from '../../../api/services';
import { BuildDialogComponent } from '../build-dialog/build-dialog.component';
import { Router } from '@angular/router';
import { BuildInfoComponent } from '../build-info/build-info.component';
import { SmartTableCustomActionsComponent } from '../smart-table-custom-actions/smart-table-custom-actions.component';
import { timer, Subscription } from 'rxjs';
import { MessageBus } from '../../../@core/message-bus';

@Component({
  selector: 'builds-list',
  templateUrl: './builds-list.component.html',
  styleUrls: ['./builds-list.component.scss'],
})
export class BuildsListComponent implements OnInit, OnChanges {

  subscription: Subscription;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.application.currentValue.id) {
      this.loadBuilds();
    }
  }

  @Input() application: Application;

  builds: Build[] = [];

  testBuilds: Build[] = [];

  deploymentBuilds: Build[] = [];

  query: string = '';

  settings = {
    columns: {
      id: {
        title: 'Build ID',
      },
      description: {
        title: 'Build Description',
      },
      status: {
        title: 'Status',
      },
      triggeredBy: {
        title: 'Built By',
      },
      promoted: {
        title: 'Promotion Status',
      },
      custom: {
        type: 'custom',
        renderComponent: SmartTableCustomActionsComponent,
        title: 'Actions',
      },
    },
    actions: false,
    hideSubHeader: true,
  };

  constructor(private dialogService: NbDialogService,
    private applicationControllerService: ApplicationControllerService, private router: Router,
    private messageBus: MessageBus) { }

  ngOnInit() {
    this.messageBus.buildTopic.subscribe(x => this.loadBuilds());
    this.loadBuilds();
  }

  onCustom(event) {
    if (event.action === 'view') {
      this.dialogService.open(BuildInfoComponent, { context: { build: event.data } });
    }
    if (event.action === 'deploy') {
      this.router.navigate(['pages', 'applications', this.application.applicationFamily, this.application.id, "builds", event.data.id]);
    }
  }

  loadBuilds() {
    if (!this.application.id) {
      return;
    }
    this.applicationControllerService.getBuildsUsingGET(
      {
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
      },
    ).subscribe(
      builds => {
        builds = builds.filter(x => x.id.startsWith(this.query));
        if (JSON.stringify(builds) !== JSON.stringify(this.builds)) {
          this.builds = builds;
          if (this.builds.filter(x => x.status === 'IN_PROGRESS').length > 0 && (!this.subscription || this.subscription.closed)) {
            this.subscription = timer(0, 10000).subscribe(x => this.loadBuilds());
          } else if (this.subscription) {
              this.subscription.unsubscribe();
          }
        }

        this.filterBuilds();
      },
    );
  }

  showBuildDialog() {
    const dialogRef: NbDialogRef<BuildDialogComponent> = this.dialogService.open(BuildDialogComponent,
      { context: { application: this.application } });
    dialogRef.onClose.subscribe(x => this.loadBuilds());
  }

  filterBuilds() {
      this.testBuilds = this.builds.filter(x => x.testBuild).filter(x => x.id.startsWith(this.query));
      this.deploymentBuilds = this.builds.filter(x => !x.testBuild).filter(x => x.id.startsWith(this.query));
  }

  search(query: string) {
    this.query = query;
    this.loadBuilds();
  }
}
