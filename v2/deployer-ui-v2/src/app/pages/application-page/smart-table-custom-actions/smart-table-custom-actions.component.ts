import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Build } from '../../../api/models';
import { NbDialogConfig, NbDialogService } from '@nebular/theme';
import { BuildInfoComponent } from '../build-info/build-info.component';
import { TestBuildDetailsComponent } from '../test-build-details/test-build-details.component';
import { BuildLogsComponent } from '../build-logs/build-logs.component';
import { BuildPromoteComponent} from '../build-promote/build-promote.component';
import { Router, ActivatedRoute } from '@angular/router';
import { ApplicationControllerService } from '../../../api/services';
import { MessageBus } from '../../../@core/message-bus';

@Component({
  selector: 'smart-table-custom-actions',
  templateUrl: './smart-table-custom-actions.component.html',
  styleUrls: ['./smart-table-custom-actions.component.scss']
})
export class SmartTableCustomActionsComponent implements OnInit {

  @Input() rowData: any;

  constructor(private dialogService: NbDialogService, private router: Router,
    private route: ActivatedRoute, private applicationControllerService: ApplicationControllerService,
    private messageBus: MessageBus) { }

  ngOnInit() {
  }

  showBuildDetails() {
    this.dialogService.open(BuildInfoComponent, { context: { build: this.rowData } });
  }

 showTestDetails() {
    this.dialogService.open(TestBuildDetailsComponent, { context: { build: this.rowData } });
  }

  showBuildLogs() {
    this.dialogService.open(BuildLogsComponent, { context: { build: this.rowData } });
  }

  deploy() {
    this.router.navigate(['builds', this.rowData.id, 'deploy'], { relativeTo: this.route });
  }

  promoteBuild() {
     if(this.rowData.applicationFamily == "CRM"){
        const dialogRef = this.dialogService.open(BuildPromoteComponent, { context: { build: this.rowData } });

        dialogRef.onClose.subscribe(x => {
            this.messageBus.buildTopic.next(true);
        });
     }else{
        this.applicationControllerService.updateBuildUsingPUT({
              applicationFamily: this.rowData.applicationFamily,
              applicationId: this.rowData.applicationId,
              build: { promoted: true, promotionIntent: "NOT_CC_ENABLED" },
              buildId: this.rowData.id,
            }).subscribe(build => { this.rowData = build; this.messageBus.buildTopic.next(true); });
     }
  }

  openBuildPage() {
    this.router.navigate(['builds', this.rowData.id], { relativeTo: this.route });
  }
}
