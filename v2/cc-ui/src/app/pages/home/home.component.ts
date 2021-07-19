import {Component, OnInit} from '@angular/core';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {Stack} from '../../cc-api/models/stack';
import {Router} from '@angular/router';
import {SimpleOauth2User} from '../../cc-api/models/simple-oauth-2user';
import {ApplicationControllerService} from '../../cc-api/services/application-controller.service';
import {SmartTableRouterlink} from "../../components/smart-table-routerlink/smart-table-routerlink.component";

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
        valuePrepareFunction: (value, row, cell) => {
          // DATA FROM HERE GOES TO renderComponent
          const displayVal = {
            url: ["/","capc","stack",value],
            display: value
          };
          return JSON.stringify(displayVal);
        },
        renderComponent: SmartTableRouterlink,
        type: 'custom'
      },
      vcsUrl: {
        title: 'Repository',
        valuePrepareFunction: (cell) => {
          return "<a href='"+cell+"' target='_blank'>"+cell+"</a>"
        },
        type: 'html'
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
      custom: [
        {name: 'Edit', title: '<i class="eva-edit-2-outline eva"></i>', type: 'html'}]
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
    if (x.action === 'Edit') {
      const stackName = x.data.name;
      console.log('Navigate to Stack Edit' + stackName);
      this.router.navigate(['/capc', 'stack', stackName, 'edit']);
    }
  }

  createStack() {
    if (this.isUserAdmin) {
      this.router.navigate(['/capc/', 'createStack']);
    }
  }

}
