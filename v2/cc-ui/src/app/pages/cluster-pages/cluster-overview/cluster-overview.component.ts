import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UiAwsClusterControllerService} from '../../../cc-api/services/ui-aws-cluster-controller.service';
import flat from 'flat';
import { NbToastrService } from '@nebular/theme';
import { AwsClusterRequest, AbstractCluster } from 'src/app/cc-api/models';
import { UiStackControllerService } from 'src/app/cc-api/services';
import { LocalDataSource } from 'ng2-smart-table';

@Component({
  selector: 'app-cluster-overview',
  templateUrl: './cluster-overview.component.html',
  styleUrls: ['./cluster-overview.component.scss']
})
export class ClusterOverviewComponent implements OnInit {

  settings = {
    columns: {
      name: {
        title: 'Variable Name',
        filter: false,
        width: '50%',
        editable: true,
      },
      value: {
        title: 'Variable Value',
        filter: false,
        width: '50%',
        editable: true,
        editor: { type: 'text' },
      }
    },
    noDataMessage: '',
    add: {
      addButtonContent: '&#10133;',
      createButtonContent: '&#10003;',
      cancelButtonContent: '&#10005;',
      confirmCreate: true,
    },
    delete: {
      deleteButtonContent: '&#9986;',
      confirmDelete: false,
    },
    edit: {
      editButtonContent: '&#9998;',
      saveButtonContent: '&#10003;',
      cancelButtonContent: '&#10005;',
      editConfirm: true,
      confirmSave: true,
    },
    pager: {
      display: true,
      perPage: 5,
    },
    actions: {
      add: true,
      edit: true,
      delete: true,
      // class: "my-custom-class",
      // custom: [
      //   { name: 'editrecord', title: '<i>&#9986;</i>'},
      //   { name: 'deleterecord', title: '<i>&#9998;</i>' }
      // ],
      position: 'right',
    },
  };

  clusterInfo;

  cluster: AbstractCluster;

  stackUsername: string = "";
  stackPassword: string = "";
  enableSubmitForClusterOverrides: boolean = true;
  clusterVariablesSource: LocalDataSource = new LocalDataSource();
  addOverrideSpinner: boolean = false;
  stackName: string;

  constructor(
    private aWSClusterService: UiAwsClusterControllerService,
    private route: ActivatedRoute,
    private router: Router,
    private toastrService: NbToastrService,
    private stackService: UiStackControllerService) {
  }

  ngOnInit(): void {
    let clusterId = '';
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        clusterId = p.clusterId;
        this.aWSClusterService.getClusterUsingGET1(clusterId).subscribe(t => {
          this.updateTableSourceWithSecrets(t);
          this.cluster = t;
          this.clusterInfo = flat.flatten(t);
        });
      }

      this.stackName = p.stackName;
    });
  }

  updateTableSourceWithSecrets(cluster: AbstractCluster) {
    let dataSource = [];
    Object.keys(cluster.secrets).forEach(element => {
      dataSource.push({"name": element, "value": cluster.secrets[element]});
    });


    Object.keys(cluster.commonEnvironmentVariables).forEach(element => {
      dataSource.push({"name": element, "value": cluster.commonEnvironmentVariables[element]});
    });

    this.clusterVariablesSource.load(dataSource);
  }

  validateValue(event) {
    console.log(event);
    event.confirm.resolve(event.newData);
  }

  submitClusterOverrides() {
    this.enableSubmitForClusterOverrides = true;
    try {
      this.addOverrideSpinner = true;
      this.stackService.getStackUsingGET(this.stackName).subscribe(existingStack => {
        existingStack.user = this.stackUsername;
        existingStack.appPassword = this.stackPassword;
        this.stackService.createStackUsingPOST1(existingStack).subscribe(s => {
          this.addOverrideSpinner = false;
          this.toastrService.success("Updated Stack", "Success");
          this.updateCluster();
        });
      });
    } catch(err) {
      console.log(err);
      this.addOverrideSpinner = false;
      this.toastrService.danger("Error updating stack variables", "Error");
    }
  }

  private resetClusterOverrides() {
    this.addOverrideSpinner = false;
    this.clusterVariablesSource.reset();
    this.stackUsername = "";
    this.stackPassword = "";
  }

  private updateCluster() {
    this.addOverrideSpinner = true;

    let awsClusterRequest: AwsClusterRequest = {};

    awsClusterRequest = {};
    awsClusterRequest.cloud = this.cluster.cloud;
    awsClusterRequest.clusterName = this.cluster.name;
    awsClusterRequest.stackName = this.cluster.stackName;
    awsClusterRequest.clusterVars = {};
    
    this.clusterVariablesSource.getAll().then(value => {
      value.forEach(element => {
        awsClusterRequest.clusterVars[element.name] = element.value;
      });;

      try {
        this.aWSClusterService.updateClusterUsingPUT1({
          request: awsClusterRequest,
          clusterId: this.cluster.id
        }).subscribe(c => {
          console.log(c);
          this.resetClusterOverrides();
          this.addOverrideSpinner = false;
          this.toastrService.success("Updated cluster variables", "Success");
          location.reload();
        });
      } catch(err) {
        console.log(err);
        this.addOverrideSpinner = false;
        this.toastrService.danger("Updated Stack", "Error");      
      }
    });

    console.log(awsClusterRequest);
  }

  onCustomAction(event) {
    console.log(event);
  }
}
