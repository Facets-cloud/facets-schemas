import { Component, OnInit, ViewChild } from '@angular/core';
import { Application } from '../../api/models';
import { ApplicationControllerService } from '../../api/services';
import { NumberComponentDynamicComponent } from './number-component-dynamic/number-component-dynamic.component';
import { NbToastrService, NbStepperComponent, NbSelectModule } from '@nebular/theme';
import { Router, ActivatedRoute } from '@angular/router';
import { ApplicationsMenuComponent } from '../applications-menu/applications-menu.component';
import { MessageBus } from '../../@core/message-bus';
import { CtrDropdown } from 'ng2-completer';

@Component({
  selector: 'create-application-page',
  templateUrl: './create-application-page.component.html',
  styleUrls: ['./create-application-page.component.scss'],
})
export class CreateApplicationPageComponent implements OnInit {

  application: Application = {
    ports: [],
    healthCheck: {
      livenessProbe: {},
      readinessProbe: {},
    },
    pvcList: [],
  };

  appFamilies = [];

  applicationTypes = [];

  settings = {
    columns: {
      name: {
        title: 'Name',
        filter: false,
        width: '34%',
        editable: false,
      },
      containerPort: {
        title: 'Container Port',
        filter: false,
        width: '33%',
        editor: { type: 'custom', component: NumberComponentDynamicComponent },
      },
      lbPort: {
        title: 'LoadBalancer Port',
        filter: false,
        width: '33%',
        editor: { type: 'custom', component: NumberComponentDynamicComponent },
      },
    },
    noDataMessage: '',
    add: {
      addButtonContent: '<i class="nb-plus"></i>',
      createButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmCreate: true,
    },
    delete: {
      deleteButtonContent: '<i class="nb-trash"></i>',
      confirmDelete: false,
    },
    edit: {
      editButtonContent: '<i class="nb-edit"></i>',
      saveButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      editConfirm: true,
      confirmSave: true,
    },
    pager: {
      display: true,
      perPage: 5,
    },
    actions: {
      position: 'right',
    },
  };


  persistantVolumeLayout = {
    columns: {
      name: {
        title: 'Name',
        filter: false,
        width: '20%',
        editable: false,
      },
      accessMode: {
        title: 'Access Mode',
        filter: false,
        width: '20%',
        defaultValue: 'ReadWriteOnce',
        editor: {
          type: 'list',
          config: {
            selectText: 'Select..',
            list: [
              {value: 'ReadWriteOnce', title: 'ReadWriteOnce'},
              {value: 'ReadOnlyMany', title: 'ReadOnlyMany'},
              {value: 'ReadWriteMany', title: 'ReadWriteMany'},
            ],
          },
        },
      },
      storageSize: {
        title: 'Storage Size (Gi)',
        filter: false,
        width: '20%',
        editor: { type: 'custom', component: NumberComponentDynamicComponent },
      },
      mountPath: {
        title: 'Path',
        filter: false,
        width: '20%',
      },
    },
    noDataMessage: '',
    add: {
      addButtonContent: '<i class="nb-plus"></i>',
      createButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmCreate: true,
    },
    edit: {
      editButtonContent: '<i class="nb-edit"></i>',
      saveButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmSave: true,
    },
    pager: {
      display: true,
      perPage: 5,
    },
    actions: {
      position: 'right',
    },
  };

  constructor(private applicationControllerService: ApplicationControllerService,
    private nbToastrService: NbToastrService, private router: Router,
    private messageBus: MessageBus, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      paramMap => {
        if (paramMap.has('appFamily') && paramMap.has('applicationId')) {
          this.applicationControllerService.getApplicationUsingGET({
            applicationFamily: <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>paramMap.get('appFamily'),
            applicationId: paramMap.get('applicationId'),
          }).subscribe(app => {
            if (!app.ports) {
              app.ports = [];
            }
            if (!app.pvcList) {
              app.pvcList = [];
            }
            if (!app.healthCheck) {
              app.healthCheck = {
                livenessProbe: {},
                readinessProbe: {},
              };
            }
            if (!app.healthCheck.livenessProbe) {
              app.healthCheck.livenessProbe = {
              };
            }
            if (!app.healthCheck.readinessProbe) {
              app.healthCheck.readinessProbe = {
              };
            }
            this.application = app;
          });
        }
      },
    );
    this.populateApplicationTypes();
    this.populateAppFamilies();
  }

  populateAppFamilies() {
    this.applicationControllerService.getApplicationFamiliesUsingGET().subscribe(
      appFamilies => {
        this.appFamilies = appFamilies;
      },
    );
  }

  populateApplicationTypes() {
    this.applicationControllerService.getApplicationTypesUsingGET().subscribe(
      applicationTypes => {
        // filter stateful set out as UI not implemented for it yet
        this.applicationTypes = applicationTypes; // .filter(x => x !== 'STATEFUL_SET');
      },
    );
  }

  appFamilySelected(stepper: any) {

  }

  applicationTypeSelected(stepper: any) {

  }

  validatePort(event) {
    if (this.application.ports.map(x => x.name).includes(event.newData['name'])) {
      this.nbToastrService.danger('Duplicate port name not allowed', 'Error');
      event.confirm.reject();
    }

    if (!/^[a-z]+$/.test(event.newData['name'])) {
      this.nbToastrService.danger('Invalid port name: ' + event.newData['name'] +
      '. Port name can only contain lower case alphabets', 'Error');
      event.confirm.reject();
    }

    event.confirm.resolve(event.newData);
  }

  validateEditPort(event) {
    if (!/^[a-z]+$/.test(event.newData['name'])) {
      this.nbToastrService.danger('Invalid port name: ' + event.newData['name'] +
      '. Port name can only contain lower case alphabets', 'Error');
      event.confirm.reject();
    }

    event.confirm.resolve(event.newData);
  }

  validatePVC(event) {
    if (this.application.pvcList.map(x => x.name).includes(event.newData['name'])) {
      this.nbToastrService.danger('Duplicate PVC name not allowed', 'Error');
      event.confirm.reject();
      return;
    }

    if (!/^[a-z]+$/.test(event.newData['name'])) {
      this.nbToastrService.danger('Invalid PVC name: ' + event.newData['name'] +
      '. PVC name can only contain lower case alphabets', 'Error');
      event.confirm.reject();
      return;
    }

    if (!/^[1-9][0-9]+$/.test(event.newData['storageSize'])) {
      this.nbToastrService.danger('Invalid PVC storage size: ' + event.newData['name'] +
      '. Storage size can only contain numeric values', 'Error');
      event.confirm.reject();
      return;
    }

    if (!/^[A-Za-z][A-Za-z-]*[A-Za-z]+$/.test(event.newData['mountPath'])) {
      this.nbToastrService.danger('Invlid PVC path: ' + event.newData['name'] +
      '. Path can only contain alphabets separated by -', 'Error');
      event.confirm.reject();
      return;
    }

    event.confirm.resolve(event.newData);
  }

  validateEditPVC(event) {
    if (!/^[a-z]+$/.test(event.newData['name'])) {
      this.nbToastrService.danger('Invalid PVC name: ' + event.newData['name'] +
      '. PVC name can only contain lower case alphabets', 'Error');
      event.confirm.reject();
      return;
    }

    if (!/^[1-9][0-9]+$/.test(event.newData['storageSize'])) {
      this.nbToastrService.danger('Invalid PVC storage size: ' + event.newData['name'] +
      '. Storage size can only contain numeric values', 'Error');
      event.confirm.reject();
      return;
    }

    if (!/^[A-Za-z][A-Za-z-]*[A-Za-z]+$/.test(event.newData['mountPath'])) {
      this.nbToastrService.danger('Invlid PVC path: ' + event.newData['name'] +
      '. Path can only contain alphabets separated by -', 'Error');
      event.confirm.reject();
      return;
    }

    event.confirm.resolve(event.newData);
  }

  onDeletePVC(event) {
    for (let i = 0; i < this.application.pvcList.length; i++) {
      if (this.application.pvcList[i].name === event.data['name']) {
        this.application.pvcList.splice(i, 1);
      }
    }
    event.confirm.resolve(event.data);
  }

  skipLiveliness(stepper: NbStepperComponent) {
    this.application.healthCheck.livenessProbe = {};
    stepper.next();
  }

  skipReadiness(stepper: NbStepperComponent) {
    this.application.healthCheck.readinessProbe = {};
    stepper.next();
  }

  createOrUpdateApplication() {
    this.application.dnsType = this.application.loadBalancerType === 'EXTERNAL' ? 'PUBLIC' : 'PRIVATE';

    // Set default thresholds as 1 is the only allowed value by kubernetes
    this.application.healthCheck.livenessProbe.successThreshold = 1;
    this.application.healthCheck.readinessProbe.successThreshold = 1;

    if (this.application.id) {
      this.updateApplication();
    } else {
      this.createApplication();
    }
  }

  private navigateToApplication(app: Application) {
    this.messageBus.application.next(true);
    this.router.navigate(['pages', 'applications', app.applicationFamily, app.id]);
  }

  private createApplication() {
    this.applicationControllerService.createApplicationUsingPOST({
      applicationFamily: this.application.applicationFamily,
      application: this.application,
    }).subscribe(app => {
      this.navigateToApplication(app);
    });
  }

  private updateApplication() {
    this.applicationControllerService.updateApplicationUsingPUT({
      applicationFamily: this.application.applicationFamily,
      application: this.application,
    }).subscribe(app => {
      this.navigateToApplication(app);
    });
  }
}
