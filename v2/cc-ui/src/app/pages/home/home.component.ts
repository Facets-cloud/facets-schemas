import {Component, OnInit} from '@angular/core';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {Stack} from '../../cc-api/models/stack';
import {Router} from '@angular/router';
import {SimpleOauth2User} from '../../cc-api/models/simple-oauth-2user';
import {ApplicationControllerService } from '../../cc-api/services/application-controller.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  stacks: Stack[];
  tableSettings = {
    columns: {
      name: {
        title: 'Stack Name',
      },
      vcsUrl: {
        title: 'Repository',
      },
      relativePath: {
        title: 'Path',
      },
      user: {
        title: 'Repo User',
      }
    },
    actions: {
      edit: false,
      delete: false,
      add: false,
      position: 'right',
      custom: [{name: 'View', title: '<i class="eva-eye-outline eva"></i>', type: 'html'}]
    },
    hideSubHeader: true,
  };

  constructor(private uiStackControllerService: UiStackControllerService,
              private router: Router,
              private applicationController: ApplicationControllerService) {
  }

  user: SimpleOauth2User;
  isUserAdmin: any;

  ngOnInit(): void {
    this.uiStackControllerService.getStacksUsingGET1().subscribe(
      s => {
        return this.stacks = s;
      }
    );

    this.applicationController.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
        this.isUserAdmin = (this.user.authorities.map(x => x.authority).includes('ROLE_ADMIN'))
        || this.user.authorities.map(x => x.authority).includes('ROLE_CC-ADMIN');
      }
    );
  }

  gotoPage(x): void {
    if (x.action === 'View') {
      const stackName = x.data.name;
      console.log('Navigate to Stack ' + stackName);
      this.router.navigate(['/capc', 'stack', stackName]);
    }
  }

  createStack() {
    if (this.isUserAdmin){
      this.router.navigate(['/capc/', 'createStack']);
      }
  }

}
