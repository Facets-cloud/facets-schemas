import {VariableDetails} from './../../../cc-api/models/variable-details';
import {Component, OnInit} from '@angular/core';
import {UiArtifactoryControllerService, UiStackControllerService} from 'src/app/cc-api/services';
import {ActivatedRoute, Router} from '@angular/router';
import {Stack} from './../../../cc-api/models/stack';
import {LocalDataSource} from 'ng2-smart-table';
import {NbToastrService} from "@nebular/theme";

@Component({
  selector: 'app-stack-create',
  templateUrl: './stack-create.component.html',
  styleUrls: ['./stack-create.component.scss']
})
export class StackCreateComponent implements OnInit {

  stack: Stack = {
    name: '',
    vcs: "BITBUCKET",
    appPassword: '',
    childStacks: [],
    clusterVariablesMeta: {},
    pauseReleases: false,
    relativePath: '',
    stackVars: {},
    user: '',
    vcsUrl: ''
  };
  childStacks: [];
  subStacks: [];
  clusterVarsTable: LocalDataSource = new LocalDataSource();
  stackVarsTable: LocalDataSource = new LocalDataSource();
  boolList = [
    {title: 'true', value: 'true'},
    {title: 'false', value: 'false'},
  ];
  varDetails: VariableDetails;
  settingsClusterVars = {
    columns: {
      name: {
        title: 'Name',
        filter: false,
        width: '35%',
        editable: false,
      },
      value: {
        title: 'Value',
        filter: false,
        width: '35%',
        editable: false,
      },
      required: {
        title: 'Required',
        filter: false,
        width: '12%',
        editable: true,
        editor: {
          type: 'list',
          config: {
            list: this.boolList,
          },
        },
      },
      secret: {
        title: 'Secret',
        filter: false,
        width: '12%',
        editable: true,
        editor: {
          type: 'list',
          config: {
            list: this.boolList,
          },
        },
      },
    },
    noDataMessage: '',
    pager: {
      display: true,
      perPage: 5,
    },
    actions: {
      add: true,
      edit: true,
      delete: true,
      position: 'right',
    },
    edit: {
      inputClass: '',
      editButtonContent: '<i class="eva-edit-outline eva"></i>',
      saveButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmSave: false,
    },
    add: {
      addButtonContent: '<i class="eva-plus-outline eva"></i>',
      createButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmCreate: true,
    },
    delete: {
      deleteButtonContent: '<i class="eva-trash-outline eva"></i>',
      confirmDelete: false,
    }
  };
  settings = {
    columns: {
      name: {
        title: 'Name',
        filter: false,
        width: '50%',
        editable: false,
      },
      value: {
        title: 'Value',
        filter: false,
        width: '50%',
        editable: true,
        editor: {type: 'text'},
      }
    },
    noDataMessage: '',
    pager: {
      display: true,
      perPage: 5,
    },
    actions: {
      add: true,
      edit: true,
      delete: true,
      position: 'right',
    },
    edit: {
      inputClass: '',
      editButtonContent: '<i class="eva-edit-outline eva"></i>',
      saveButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmSave: false,
    },
    add: {
      addButtonContent: '<i class="eva-plus-outline eva"></i>',
      createButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmCreate: true,
    },
    delete: {
      deleteButtonContent: '<i class="eva-trash-outline eva"></i>',
      confirmDelete: false,
    }
  };
  private errorMessage: any;
  artifactories: any = [];

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private uiStackControllerService: UiStackControllerService, private toastrService: NbToastrService,
              private artifactoryControllerService: UiArtifactoryControllerService, ) {
  }

  ngOnInit(): void {
    this.artifactoryControllerService.getAllArtifactoriesUsingGET1()
      .subscribe(arts => arts.map(a => a.name).forEach(n => this.artifactories.push({"value": n, "label": n})))
  }

  async createStack() {
    try {
      const stackVarsDataSource = await this.stackVarsTable.getAll();
      stackVarsDataSource.forEach(ele => {
        this.stack.stackVars[ele.name] = ele.value;
      })
      const clusterVarsDataSource = await this.clusterVarsTable.getAll();
      clusterVarsDataSource.forEach(element => {
        this.varDetails = {required: element.required, secret: element.secret, value: element.value}
        this.stack.clusterVariablesMeta[element.name] = this.varDetails
        this.varDetails = null;
      });

      console.log(this.stack);
      this.uiStackControllerService.createStackUsingPOST1(this.stack)
        .subscribe(cluster => {
            this.router.navigate(['/capc/', 'home']);
          },
          error => {
            this.toastrService.danger('Stack creation failed ' + error.statusText, 'Error', {duration: 8000});
          });
    } catch (err) {
      console.log(err);
      //this.toastrService.danger('Cluster creation failed', 'Error', {duration: 5000});
    }
  }


  validateValue(event) {
    console.log(event);
    event.confirm.resolve(event.newData);
  }

  test() {
    debugger;
  }
}
