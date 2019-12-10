import { Component, OnInit, Input } from '@angular/core';
import { Application, ApplicationSecretRequest, ApplicationSecret, Environment } from '../../../api/models';
import { NbDialogRef } from '@nebular/theme';
import { ApplicationControllerService } from '../../../api/services';

@Component({
  selector: 'set-credential-value-dialog',
  templateUrl: './set-credential-value-dialog.component.html',
  styleUrls: ['./set-credential-value-dialog.component.scss']
})
export class SetCredentialValueDialogComponent implements OnInit {

  @Input() application: Application;

  @Input() secretName: string;

  @Input() environment: string;

  @Input() secretType: 'ENVIRONMENT' | 'FILE';

  applicationSecret: ApplicationSecret = {};

  constructor(protected ref: NbDialogRef<SetCredentialValueDialogComponent>,
    private applicationControllerService: ApplicationControllerService) { }

  ngOnInit() {
  }

  onSubmit() {
    this.applicationSecret.secretName = this.secretName;
    this.applicationControllerService.updateApplicationSecretsUsingPUT({
      environment: this.environment,
      applicationFamily: this.application.applicationFamily,
      applicationId: this.application.id,
      applicationSecrets: [this.applicationSecret],
    }).subscribe(x => {this.ref.close(); });
  }
}
