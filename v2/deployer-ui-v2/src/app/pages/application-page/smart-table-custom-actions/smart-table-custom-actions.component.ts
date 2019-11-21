import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Build } from '../../../api/models';
import { NbDialogConfig, NbDialogService } from '@nebular/theme';
import { BuildInfoComponent } from '../build-info/build-info.component';
import { BuildLogsComponent } from '../build-logs/build-logs.component';
import { Router, ActivatedRoute } from '@angular/router';
import { ApplicationControllerService } from '../../../api/services';
import { MessageBus } from '../../../@core/message-bus';

@Component({
  selector: 'smart-table-custom-actions',
  templateUrl: './smart-table-custom-actions.component.html',
  styleUrls: ['./smart-table-custom-actions.component.scss']
})
export class SmartTableCustomActionsComponent implements OnInit {

  @Input() rowData: Build;

  constructor(private dialogService: NbDialogService, private router: Router,
    private route: ActivatedRoute, private applicationControllerService: ApplicationControllerService,
    private messageBus: MessageBus) { }

  ngOnInit() {
  }

  showBuildDetails() {
    this.dialogService.open(BuildInfoComponent, { context: { build: this.rowData } });
  }

  showBuildLogs() {
    this.dialogService.open(BuildLogsComponent, { context: { build: this.rowData } });
  }

  deploy() {
    this.router.navigate(['builds', this.rowData.id, 'deploy'], { relativeTo: this.route });
  }

  promoteBuild() {
    this.applicationControllerService.updateBuildUsingPUT({
      applicationFamily: this.rowData.applicationFamily,
      applicationId: this.rowData.applicationId,
      build: { promoted: true },
      buildId: this.rowData.id,
    }).subscribe(build => { this.rowData = build; this.messageBus.buildTopic.next(true); });
  }

  openBuildPage() {
    this.router.navigate(['builds', this.rowData.id], { relativeTo: this.route });
  }
}
