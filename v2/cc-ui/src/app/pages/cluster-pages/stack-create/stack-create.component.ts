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

  private errorMessage: any;
  artifactories: any = [];
  isCreate: boolean = true;

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private route: ActivatedRoute,
              private uiStackControllerService: UiStackControllerService, private toastrService: NbToastrService,
              private artifactoryControllerService: UiArtifactoryControllerService,) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (p.stackName) {
        this.isCreate = false;
        this.uiStackControllerService.getStackUsingGET(p.stackName).subscribe(
          s => this.stack = s
        )
      }
    });

    this.artifactoryControllerService.getAllArtifactoriesUsingGET1()
      .subscribe(arts => arts.map(a => a.name).forEach(n => this.artifactories.push({"value": n, "label": n})))
  }

  createStack() {
    try {
      this.uiStackControllerService.createStackUsingPOST1(this.stack)
        .subscribe(cluster => {
            this.toastrService.success('Stack creation success!! ', 'Success', {duration: 2000});
            setTimeout(() => {
              this.router.navigate(['/capc/', 'home'])
            }, 2000);
          },
          error => {
            this.toastrService.danger('Stack creation failed ' + error.statusText, 'Error', {duration: 8000});
          });
    } catch (err) {
      console.log(err);
      //this.toastrService.danger('Cluster creation failed', 'Error', {duration: 5000});
    }
  }

  updateStack() {
    try {
      this.uiStackControllerService.updateStackUsingPUT1({stackName: this.stack.name, stack: this.stack})
        .subscribe(cluster => {
            this.toastrService.success('Stack change success!! ', 'Success', {duration: 2000});
            setTimeout(() => {
              this.router.navigate(['/capc/', 'home'])
            }, 2000);
          },
          error => {
            this.toastrService.danger('Stack change failed ' + error.statusText, 'Error', {duration: 8000});
          });
    } catch (err) {
      console.log(err);
      //this.toastrService.danger('Cluster creation failed', 'Error', {duration: 5000});
    }
  }

  toHome() {
    this.router.navigate(['/capc/', 'home'])
  }
}
