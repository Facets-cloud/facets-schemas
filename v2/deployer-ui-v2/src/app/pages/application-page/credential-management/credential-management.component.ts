import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Application, EnvironmentMetaData, ApplicationSecret, ApplicationSecretRequest, Environment, SimpleOauth2User } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';
import { SecretStatusColumnComponent } from '../secret-status-column/secret-status-column.component';
import { NbDialogService, NbDialogRef } from '@nebular/theme';
import { BuildDialogComponent } from '../build-dialog/build-dialog.component';
import { RequestCredentialDialogComponent } from '../request-credential-dialog/request-credential-dialog.component';
import { UserService } from '../../../@core/mock/users.service';
import { User } from '../../../@core/data/users';

@Component({
  selector: 'credential-management',
  templateUrl: './credential-management.component.html',
  styleUrls: ['./credential-management.component.scss']
})
export class CredentialManagementComponent implements OnInit, OnChanges {
  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  @Input() application: Application;

  environments: Array<EnvironmentMetaData>;

  credentialRequests: Array<ApplicationSecretRequest>;

  secretsMap: { [environment: string]: Array<ApplicationSecret> } = {};

  tableData: Array<any>;

  settings: any;

  user: SimpleOauth2User;

  getSettings() {
    const ret = {
      columns: {
        secretName: {
          title: 'Name',
        },
        description: {
          title: 'Description',
        },
      },
      actions: false,
      hideSubHeader: true,
    };
    this.environments.forEach(x => ret.columns[x.name] =
      {
        title: x.name,
        type: 'custom',
        renderComponent: SecretStatusColumnComponent,
        onComponentInitFunction: (instance) => {
          instance.updateResult.subscribe(updatedUserData => {
            this.ngOnInit();
          });
        },
      });
    return ret;
  }

  constructor(private applicationControllerService: ApplicationControllerService,
    private dialogService: NbDialogService, private userService: UserService) { }

  ngOnInit() {
    this.userService.getUser().subscribe(user => this.user = user);
    if (!this.application.applicationFamily) {
      return;
    }
    this.applicationControllerService.getEnvironmentMetaDataUsingGET(this.application.applicationFamily)
      .subscribe(x => {
        this.environments = x;
        this.settings = this.getSettings();
        this.applicationControllerService.getApplicationSecretRequestsUsingGET({
          applicationFamily: this.application.applicationFamily,
          applicationId: this.application.id,
        }).subscribe(y => {
          this.credentialRequests = y;
          this.loadSecrets();
        });
      });
  }

  async loadSecrets() {
    for (const environment of this.environments) {
      const secrets: Array<ApplicationSecret> = await this.applicationControllerService.getApplicationSecretsUsingGET({
        environment: environment.name,
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
      }).toPromise();
      if (secrets) {
        this.secretsMap[environment.name] = secrets;
      } else {
        this.secretsMap[environment.name] = [];
      }
    }
    this.loadTableData();
  }

  loadTableData() {
    const tableData = [];
    this.credentialRequests.forEach(
      request => {
        const row = {};
        row['secretName'] = request.secretName;
        row['description'] = request.description;
        row['allowEdit'] = this.user.authorities.map(x => x.authority).includes('ROLE_ADMIN');
        row['application'] = this.application;
        this.environments.forEach(environment => {
          row[environment.name] = {secretName: request.secretName, environment: environment.name, secretStatus: 'PENDING'};
          const secrets = this.secretsMap[environment.name];
          secrets.forEach(secret => {
            if (secret.secretName === request.secretName) {
              row[environment.name] = {secretName: request.secretName, environment: environment.name, secretStatus: secret.secretStatus};
            }
          });
        });
        tableData.push(row);
      },
    );
    this.tableData = tableData;
  }

  requestCredential() {
    const dialogRef: NbDialogRef<RequestCredentialDialogComponent> =
      this.dialogService.open(RequestCredentialDialogComponent, { context: { application: this.application } });
    dialogRef.onClose.subscribe(x => this.ngOnInit());
  }

}
