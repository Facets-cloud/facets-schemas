import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UiAwsClusterControllerService} from '../../../cc-api/services/ui-aws-cluster-controller.service';
import {ApplicationControllerService } from '../../../cc-api/services/application-controller.service';
import flat from 'flat';
import {SimpleOauth2User} from '../../../cc-api/models/simple-oauth-2user';
import {NbSelectModule, NbToastrService} from '@nebular/theme';
import {NbToggleModule} from '@nebular/theme';
import {AwsClusterRequest, AbstractCluster} from 'src/app/cc-api/models';
import {UiDeploymentControllerService, UiStackControllerService} from 'src/app/cc-api/services';
import {LocalDataSource} from 'ng2-smart-table';
import {AwsCluster} from '../../../cc-api/models/aws-cluster';
import {element} from 'protractor';
import { analyzeAndValidateNgModules } from '@angular/compiler';

@Component({
  selector: 'app-cluster-overview',
  templateUrl: './cluster-overview.component.html',
  styleUrls: ['./cluster-overview.component.scss']
})

export class ClusterOverviewComponent implements OnInit {
  releaseTypes = ['Release', 'Hotfix'];
  releaseTypeSelection: any = 'Release';
  applicationName: any = '';
  payload: any = '{}';
  user: SimpleOauth2User;

  settings = {
    columns: {
      name: {
        title: 'Variable Name',
        filter: false,
        width: '50%',
        editable: false,
      },
      value: {
        title: 'Variable Value',
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
      add: false,
      edit: true,
      delete: false,
      // class: "my-custom-class",
      // custom: [
      //   { name: 'editrecord', title: '<i>&#9986;</i>'},
      //   { name: 'deleterecord', title: '<i>&#9998;</i>' }
      // ],
      position: 'right',
    },
    edit: {
      inputClass: '',
      editButtonContent: '<i class="eva-edit-outline eva"></i>',
      saveButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmSave: false,
    },
  };

  clusterInfo;

  cluster: AwsCluster;

  enableSubmitForClusterOverrides = true;
  nonSensitiveClusterSource: LocalDataSource = new LocalDataSource();
  sensitiveClusterSource: LocalDataSource = new LocalDataSource();
  originalClusterVariablesSource = [];
  addOverrideSpinner = false;
  stackName: string;
  extraEnvVars = ['TZ', 'CLUSTER', 'AWS_REGION'];
  isUserAdmin: any;

  constructor(private aWSClusterService: UiAwsClusterControllerService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService,
              private stackService: UiStackControllerService,
              private deploymentService: UiDeploymentControllerService,
              private applicationController: ApplicationControllerService) {
  }

  ngOnInit(): void {
    let clusterId = '';
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        clusterId = p.clusterId;
        this.aWSClusterService.getClusterUsingGET1(clusterId).subscribe(t => {
          this.updateTableSourceWithStackVariables(t);
          this.cluster = t;
          this.clusterInfo = flat.flatten(t);
        });
      }

      this.stackName = p.stackName;
    });
    this.applicationController.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
        this.isUserAdmin = (this.user.authorities.map(x => x.authority).includes('ROLE_ADMIN'))
        || this.user.authorities.map(x => x.authority).includes('ROLE_USER_ADMIN');
      }
    );
  }

  updateTableSourceWithStackVariables(cluster: AbstractCluster) {
    let dataSource = [];
    Object.keys(cluster.commonEnvironmentVariables).forEach(element => {
      if (!this.extraEnvVars.includes(element)) {
        dataSource.push({name: element, value: cluster.commonEnvironmentVariables[element]});
        const clone = {name: element, value: cluster.commonEnvironmentVariables[element]};
        this.originalClusterVariablesSource.push(clone);
      }
    });
    this.nonSensitiveClusterSource.load(dataSource);

    dataSource = [];
    Object.keys(cluster.secrets).forEach(element => {
      dataSource.push({name: element, value: cluster.secrets[element]});
      const clone = {name: element, value: cluster.secrets[element]};
      this.originalClusterVariablesSource.push(clone);
    });
    this.sensitiveClusterSource.load(dataSource);
  }

  validateValue(event) {
    console.log(event);
    event.confirm.resolve(event.newData);
  }

  submitClusterOverrides() {
    this.enableSubmitForClusterOverrides = true;
    try {
      this.updateCluster();
    } catch (err) {
      console.log(err);
      this.addOverrideSpinner = false;
      // this.toastrService.danger('Error updating stack variables', 'Error');
    }
  }

  triggerHotfixApply() {
    this.addOverrideSpinner = true;
    console.log(this.releaseTypeSelection);
    console.log(this.applicationName);
    console.log(this.user);
    const applicationNameArray = this.applicationName.split(',');
    let targetsForOverride = '';
    for (let i = 0; i < applicationNameArray.length; i++) {
      applicationNameArray[i] = applicationNameArray[i].replace(/^\s*/, '').replace(/\s*$/, '');
      targetsForOverride = targetsForOverride.concat(' -target \'module.application.helm_release.application[\"' + applicationNameArray[i] + '\"]\'');
    }
    if (this.releaseTypeSelection === 'Hotfix'){
      this.payload = {
        releaseType: 'RELEASE',
        overrideBuildSteps: 'terraform apply ' + targetsForOverride + ' -auto-approve '
      };
    }
    else
    {
      this.payload = {
        releaseType: 'RELEASE'
      };
    }
    console.log(this.payload);
    try {
      this.deploymentService.createDeploymentUsingPOST1(
        this.cluster.id,
        this.payload
      );
      this.addOverrideSpinner = false;
    } catch (err) {
      console.log(err);
      this.addOverrideSpinner = false;
    }
  }

  refreshStack() {
    this.addOverrideSpinner = true;
    try {
      this.stackService.reloadStackUsingGET1(this.cluster.stackName).subscribe(stack => {
        this.addOverrideSpinner = false;
        location.reload();
      });
    } catch (err) {
      console.log(err);
      this.addOverrideSpinner = false;
    }
  }

  private resetClusterOverrides() {
    this.addOverrideSpinner = false;
    this.nonSensitiveClusterSource.reset();
    this.sensitiveClusterSource.reset();
  }

  private hasClusterVariableChanged(source, variableName: string) {
    let originalValue = null;
    this.originalClusterVariablesSource.forEach(element => {
      if (element.name === variableName) {
        originalValue = element.value;
      }
    });

    let newValue = null;
    source.forEach(element => {
      if (element.name === variableName) {
        newValue = element.value;
      }
    });

    if (originalValue && newValue != originalValue) {
      return true;
    }

    return false;
  }

  isEmptyObject(obj: Object) {
    return (obj && (Object.keys(obj).length === 0));
  }

  private async updateCluster() {
    this.addOverrideSpinner = true;

    const awsClusterRequest: AwsClusterRequest = await this.constructUpdateClusterRequest();

    if (this.isEmptyObject(awsClusterRequest.clusterVars)) {
      this.toastrService.danger('No variables have changed, cannot update', 'Error');
      this.addOverrideSpinner = false;
      return;
    }

    try {
      this.aWSClusterService.updateClusterUsingPUT1({
        request: awsClusterRequest,
        clusterId: this.cluster.id
      }).subscribe(c => {
        console.log(c);
        this.resetClusterOverrides();
        this.addOverrideSpinner = false;
        this.toastrService.success('Updated cluster variables', 'Success');
        location.reload();
      });
    } catch (err) {
      console.log(err);
      this.addOverrideSpinner = false;
      this.toastrService.danger('Cannot update stack', 'Error');
    }

    console.log(awsClusterRequest);
  }

  private async constructUpdateClusterRequest() {
    let awsClusterRequest: AwsClusterRequest = {};

    awsClusterRequest = {};
    awsClusterRequest.cloud = this.cluster.cloud;
    awsClusterRequest.clusterName = this.cluster.name;
    awsClusterRequest.stackName = this.cluster.stackName;
    awsClusterRequest.clusterVars = {};

    const nonSensitiveSource = await this.nonSensitiveClusterSource.getAll();
    nonSensitiveSource.forEach(element => {
      if (this.hasClusterVariableChanged(nonSensitiveSource, element.name)) {
        awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });

    const sensitiveSource = await this.sensitiveClusterSource.getAll();
    sensitiveSource.forEach(element => {
      if (this.hasClusterVariableChanged(sensitiveSource, element.name)) {
        awsClusterRequest.clusterVars[element.name] = element.value;
      }
    });

    return awsClusterRequest;
  }
}
