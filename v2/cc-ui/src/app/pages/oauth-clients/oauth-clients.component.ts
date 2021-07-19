import {Component, OnInit} from '@angular/core';
import {UiOAuthControllerService} from "../../cc-api/services/ui-oauth-controller.service";
import {CustomOAuth2ClientRegistration} from "../../cc-api/models/custom-oauth-2client-registration";
import {NbDialogService} from "@nebular/theme";

@Component({
  selector: 'app-oauth-clients',
  templateUrl: './oauth-clients.component.html',
  styleUrls: ['./oauth-clients.component.scss']
})
export class OauthClientsComponent implements OnInit {
  integrations: Array<CustomOAuth2ClientRegistration>;
  google: CustomOAuth2ClientRegistration = {
    provider: "GOOGLE",
    scope: "email",
  };
  tableSettings: any = {
    columns: {
      registrationId: {
        title: 'Registration Id',
      },
      provider: {
        title: 'Provider',
      },
      loginButtonText: {
        title: 'Login Button',
      },
      clientId: {
        title: 'Client Id',
        filter: false,
      },
      scope: {
        title: 'Scope',
        filter: false,
      },
      systemConfigured: {
        title: 'Type',
        filter: false,
        valuePrepareFunction: (cell) => {
          if (cell) {
            return 'System Defined'
          } else {
            return 'Custom'
          }
        }
      }
    },
    noDataMessage: 'No Integrations. Create your first OAuth Integration',
    pager: {
      display: false,
      perPage: 15,
    },
    mode: "external",
    actions: {
      add: false,
      position: 'right',
      delete: false,
      custom: [{name: 'Edit', title: '<i class="eva-edit-outline eva"></i>', type: 'html'},
        {name: 'Delete', title: '<i class="eva-close-outline eva"></i>', type: 'html'}]
    },
  };
  errorMsg: any;
  errorMsgDelete: any;

  constructor(private oAuthControllerService: UiOAuthControllerService, private dialogService: NbDialogService,) {
  }

  ngOnInit(): void {
    this.oAuthControllerService.getAllIntegrationsUsingGET().subscribe(response => {
      this.integrations = response;
    })
  }

  openGoogleCreatePopup(ref: any) {
    this.google = {
      provider: "GOOGLE",
      scope: "email",
    };
    this.errorMsg = "";
    this.dialogService.open(ref, {context: {edit: false}});
  }

  customActionPressed(event, deleteConfirmRef, editRef) {
    if (event.action == "Delete") {
      this.dialogService.open(deleteConfirmRef, {context: {id: event.data.registrationId, payload: event.data}});
    } else {
      this.google = event.data
      this.dialogService.open(editRef, {context: {edit: true}});
    }
  }

  addIntegration(google: CustomOAuth2ClientRegistration, ref) {
    this.oAuthControllerService.addIntegrationsUsingPOST(google).subscribe(
      response => {
        this.integrations = response;
        ref.close();
      },
      error => {
        this.errorMsg = error.error.message || error.message
      }
    )
  }

  editIntegration(google: CustomOAuth2ClientRegistration, ref) {
    this.oAuthControllerService.updateIntegrationsUsingPUT({
      client: google,
      registrationId: google.registrationId
    }).subscribe(
      response => {
        this.integrations = response;
        ref.close();
      },
      error => {
        this.errorMsg = error.error.message || error.message
      }
    )
    ;
  }

  removeIntegration(registrationId: string, ref: any) {

    this.oAuthControllerService.deleteIntegrationsUsingDELETE(registrationId).subscribe(
      response => {
        this.integrations = response;
        ref.close();
      },
      error => {
        this.errorMsgDelete = error.error.message || error.message
      }
    )
  }
}
