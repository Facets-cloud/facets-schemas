import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Application, Environment, ActionExecution, ApplicationAction } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';
import { NbDialogService } from '@nebular/theme';

@Component({
  selector: 'executed-actions',
  templateUrl: './executed-actions.component.html',
  styleUrls: ['./executed-actions.component.scss'],
})
export class ExecutedActionsComponent implements OnInit, OnChanges {

  @Input() application: Application;

  actionExecutions: Array<ActionExecution> = [];

  settings = {
    columns: {
      triggerTime: {
        title: 'Trigger Time',
        valuePrepareFunction: function(triggerTime) {
          const date = new Date(triggerTime);
          return date.toISOString();
        },
      },
      action: {
        title: 'Action Name',
        valuePrepareFunction: function(applicationAction: ApplicationAction) {
          return applicationAction.name;
        },
      },
      triggerStatus: {
        title: 'Trigger Status',
      },
      custom: {
        type: 'custom',
        renderComponent: ButtonActionInfoComponent,
        title: 'Actions',
      },
    },
    actions: false,
    hideSubHeader: true,
  };

  constructor(private applicationControllerService: ApplicationControllerService) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  ngOnInit() {
    this.loadExecutedActions();
  }

  loadExecutedActions() {
    if (!this.application.id) {
      return;
    }

    this.applicationControllerService
      .getExecutedActionsForApplicationUsingGET({
        applicationId: this.application.id,
        applicationFamily: this.application.applicationFamily,
      })
      .subscribe(executions => {
        this.actionExecutions = executions;
      });
  }

}

@Component({
selector: 'button-action-info-component',
  template: `
  <button nbTooltip="Action Log" nbButton appearance="ghost" (click)=showInfoDialog()>
    <nb-icon pack="eva" icon="info-outline"></nb-icon>
  </button>
  `,
})
export class ButtonActionInfoComponent implements OnInit {

  @Input() rowData: any;

  constructor(private dialogService: NbDialogService) {  }

  ngOnInit() {
  }

  showInfoDialog() {
    this.dialogService.open(DialogActionInfoComponent, {
      context: { actionExecution: this.rowData },
    });
  }
}

@Component({
  selector: 'button-action-info-component',
    template: `
    <nb-card status="actionExecution?.triggerException ? error : success">
      <nb-card-header>{{actionExecution?.triggerException ? "Error" : "Execution Log"}}</nb-card-header>
      <nb-card-body>
      <nb-list>
        <nb-list-item style="white-space: pre;" *ngFor="let x of info">
          {{ x }}
        </nb-list-item>
      </nb-list>
      </nb-card-body>
    </nb-card>
    `,
  })
  export class DialogActionInfoComponent implements OnInit {

    @Input() actionExecution: ActionExecution;

    info: Array<String> = [];

    constructor() {  }

    ngOnInit() {
      if (!this.actionExecution) {
        return;
      }

      const triggerException = this.actionExecution.triggerException;
      this.info = triggerException ? triggerException.split('\n') : this.actionExecution.output.split('\n');
    }
  }
