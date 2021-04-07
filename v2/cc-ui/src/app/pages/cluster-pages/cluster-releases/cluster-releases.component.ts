import { Resource } from './../../../cc-api/models/resource';
import { HotfixDeploymentRecipe } from './../../../cc-api/models/hotfix-deployment-recipe';
import {Component, OnInit, ViewChild} from '@angular/core';
import {
  UiDeploymentControllerService,
  UiCommonClusterControllerService,
  UiStackControllerService
} from 'src/app/cc-api/services';
import {ActivatedRoute, Router} from '@angular/router';
import {DeploymentLog, DeploymentRequest} from 'src/app/cc-api/models';
import {NbDialogService, NbToastrService, NbSelectModule} from '@nebular/theme';
import {ApplicationControllerService} from '../../../cc-api/services/application-controller.service';
import {SimpleOauth2User} from '../../../cc-api/models/simple-oauth-2user';
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import { ResourceSelectorComponent } from './../../../components/resource-selector/resource-selector.component';
import { ResourceTypeSelectorComponent } from './../../../components/resource-type-selector/resource-type-selector.component';
import { htmlAstToRender3Ast } from '@angular/compiler/src/render3/r3_template_transform';

@Component({
  selector: 'app-cluster-releases',
  templateUrl: './cluster-releases.component.html',
  styleUrls: ['./cluster-releases.component.scss'],
})

export class ClusterReleasesComponent implements OnInit {

  clusterId = '';
  deployments: DeploymentLog[];
  loading = true;
  downStreamClusters = [];
  currentSignedOffDeployment: DeploymentLog;
  user: SimpleOauth2User;
  isUserAdmin: any;
  appName: any;
  stackName: any;
  resourceMap;
  applicationNameList: any;
  cronjobNameList: any = '';
  statefulSetNameList: any = '';


  constructor(private deploymentController: UiDeploymentControllerService,
              private u: UiStackControllerService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: NbToastrService,
              private dialogService: NbDialogService,
              private deploymentService: UiDeploymentControllerService,
              private applicationController: ApplicationControllerService) {
  }

  settings = {
    columns: {
      resourceType: {
        type: 'custom',
        renderComponent: ResourceTypeSelectorComponent,
        title: 'Resource Type',
        width: '30%'
      },
      resourceName: {
        type: 'custom',
        renderComponent: ResourceSelectorComponent,
        title: 'Resource Name',
        width: '30%'
      }
    },
    actions: {
      add: false,
      edit: true,
      delete: false,
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

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (p.clusterId) {
        this.clusterId = p.clusterId;
        this.deploymentController.getDeploymentsUsingGET1(this.clusterId).subscribe(
          t => {
            this.currentSignedOffDeployment = t.currentSignedOffDeployment;
            t.deployments.forEach(deployment =>
              deployment['allowSignoff'] = deployment.status === 'SUCCEEDED' &&
                deployment.stackVersion?.length > 0 &&
                deployment.tfVersion?.length > 0 &&
                !deployment.signedOff &&
                deployment.createdOn > this.currentSignedOffDeployment?.createdOn);
            t.deployments.forEach(
              x => {
                if (x['allowSignoff']) {
                  if (t.stack.vcs === 'GITHUB') {
                    x['compareUrl'] = t.stack.vcsUrl.replace('.git', '') + '/compare/'
                      + t.currentSignedOffDeployment?.stackVersion + '...' + x.stackVersion;
                  } else if (t.stack.vcs === 'BITBUCKET') {
                    x['compareUrl'] = t.stack.vcsUrl.replace('.git', '') + '/compare/' +
                      x.stackVersion + '%0' + t.currentSignedOffDeployment?.stackVersion;
                  }
                } else {
                  x['compareUrl'] = null;
                }
              }
            );
            this.deployments = t.deployments;
            this.downStreamClusters = t.downStreamClusterNames;
          },
          () => this.loading = false
        );
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


    this.u.getResourcesByTypesUsingGET({stackName: this.stackName, resourceType: 'application'}).subscribe(
      (x: Array<string>) => {
        this.applicationNameList = x;
      }
    )

    this.u.getResourcesByTypesUsingGET({stackName: this.stackName, resourceType: 'cronjob'}).subscribe(
      (x: Array<string>) => {
        this.cronjobNameList = x;
      }
    )

    this.u.getResourcesByTypesUsingGET({stackName: this.stackName, resourceType: 'statefulsets'}).subscribe(
      (x: Array<string>) => {
        this.statefulSetNameList = x;
      }
    )
  }



  showDetails(dialog, deploymentId) {
    this.deploymentController.getDeploymentUsingGET({deploymentId: deploymentId, clusterId: this.clusterId}).subscribe(
      d => this.dialogService.open(dialog, {
        context: {
          changes: d.changesApplied,
          appDeployments: d.appDeployments,
          errors: d.errorLogs,
          overrideBuildSteps: d.overrideBuildSteps
        }
      }),
    );
  }

  confirmSignoff(signoff, deployment) {
    this.dialogService.open(signoff, {context: deployment}).onClose.subscribe(
      d => {
        this.loading = true;
        this.deploymentController.signOffDeploymentUsingPUT({deploymentId: d.id, clusterId: this.clusterId})
          .subscribe(
            x => {
              d.signedOff = x.signedOff;
              this.ngOnInit();
            },
          );
      },
    );
  }

  openTab(compareUrl) {
    window.open(compareUrl, "_blank");
  }

  errorHandler(error) {
    this.toastrService.warning(error.error.message, 'Error');
  }

  openDeploymentPopupFull(deploymentUI) {
    this.dialogService.open(deploymentUI, {context: 'NA'});
  }

  triggerFullRelease(ref: any) {
    this.loading = true;
    let dr: DeploymentRequest = {
      releaseType: "RELEASE"
    }
    ref.close();
    this.deploymentService.createDeploymentUsingPOST({
      clusterId: this.clusterId,
      deploymentRequest: dr
    }).subscribe(
      c => {
        console.log(c);
        this.toastrService.success('Triggered Full deployment', 'Success');
        this.ngOnInit();
      },
      err => {
        this.errorHandler(err);
      }
    );
  }


  openDeploymentPopup(deploymentUI) {
    this.dialogService.open(deploymentUI, {context: 'NA'}).onClose.subscribe(
      result => {
        if (result) {
          this.loading = true;
          let recipe: HotfixDeploymentRecipe = {};
          let objList: Array<Resource> = [];
          let res: Resource = {};
          this.resourceMap.forEach(x => {
            res = {};
            res.resourceType = x.split(':')[0].toString();
            res.resourceName = x.split(':')[1].toString();
            objList.push(res);
          });
          recipe.resourceList = objList;
          try {
            this.deploymentService.runHotfixDeploymentRecipeUsingPOST({
              clusterId: this.clusterId,
              deploymentRecipe: recipe
            }).subscribe(c => {
                console.log(c);
                this.toastrService.success('Triggered Hotfix', 'Success');
                this.ngOnInit();
              },
              err => {
                this.errorHandler(err);
              }
            );
          } catch (err) {
            console.log(err);
            console.log('Trigger failed');
            this.toastrService.warning('Trigger Hotfix Failed', 'Error');
          }
        }
      },
    );
  }

}
