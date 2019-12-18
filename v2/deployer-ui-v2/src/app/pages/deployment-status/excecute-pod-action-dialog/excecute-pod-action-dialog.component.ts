import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ApplicationPodDetails, Application } from '../../../api/models';
import { ApplicationAction } from '../../../api/models/application-action';
import { ApplicationControllerService } from '../../../api/services';
import { NbDialogRef, NbToastrService } from '@nebular/theme';
import { BuildDialogComponent } from '../../application-page/build-dialog/build-dialog.component';

@Component({
  selector: 'excecute-pod-action-dialog',
  templateUrl: './excecute-pod-action-dialog.component.html',
  styleUrls: ['./excecute-pod-action-dialog.component.scss']
})
export class ExcecutePodActionDialogComponent implements OnInit, OnChanges {

  @Input() applicationPodDetails: ApplicationPodDetails;

  applicationActions: Array<ApplicationAction> = [];
  filteredActions: Array<ApplicationAction> = [];
  filteredAction: ApplicationAction = {};
  executingAction: Boolean = false;

  constructor(private applicationController: ApplicationControllerService,
    protected ref: NbDialogRef<BuildDialogComponent>, private nbToastrService: NbToastrService) { }

  ngOnInit() {
    const application: Application = this.applicationPodDetails['application'];
    this.applicationController.getActionsForPodUsingGET({
      podName: this.applicationPodDetails.name,
      environment: this.applicationPodDetails['environment'],
      applicationId: application.id,
      applicationFamily: application.applicationFamily,
    }).subscribe(actions => {
      this.applicationActions = actions;
      this.filteredActions = actions;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  search(query: string) {
    this.filteredActions = this.applicationActions;
    this.filteredAction = {};
    this.filteredActions = this.filteredActions.filter(action => action.name.startsWith(query));
    this.filteredAction = this.filteredActions[0];
  }

  onSubmit() {
    this.executingAction = true;
    const application: Application = this.applicationPodDetails['application'];
    this.applicationController.executeActionOnPodUsingPOST({
      podName: this.applicationPodDetails.name,
      environment: this.applicationPodDetails['environment'],
      applicationId: application.id,
      applicationFamily: application.applicationFamily,
      applicationAction: this.filteredAction,
    }).subscribe(execution => {
      if (execution.id) {
        this.nbToastrService.success('Execution Successful!', 'Success');
      } else {
        this.nbToastrService.danger('Execution Error!', 'Error');
      }
      this.executingAction = false;
      this.ref.close();
    });
  }

}
