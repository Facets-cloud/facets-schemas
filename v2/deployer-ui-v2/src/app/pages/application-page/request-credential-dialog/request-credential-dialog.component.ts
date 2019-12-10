import { Component, OnInit, Input } from '@angular/core';
import { Application, ApplicationSecretRequest } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';
import { NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'request-credential-dialog',
  templateUrl: './request-credential-dialog.component.html',
  styleUrls: ['./request-credential-dialog.component.scss']
})
export class RequestCredentialDialogComponent implements OnInit {

  @Input() application: Application;

  secret: ApplicationSecretRequest = {
    secretType: 'ENVIRONMENT',
  };

  constructor(private applicationControllerService: ApplicationControllerService,
    protected ref: NbDialogRef<RequestCredentialDialogComponent>) { }

  ngOnInit() {
  }

  onSubmit() {
    this.applicationControllerService.createAppSecretRequestUsingPOST({
      applicationFamily: this.application.applicationFamily,
      applicationId: this.application.id,
      applicationSecretRequests: [this.secret],

    }).subscribe(x => {
      this.ref.close();
    });
  }

}
