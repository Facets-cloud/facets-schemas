import {Component, OnInit} from '@angular/core';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {Stack} from '../../cc-api/models/stack';
import {Router} from '@angular/router';

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
      custom: [{name: 'View', title: 'View'}]
    },
    hideSubHeader: true,
  };

  constructor(private uiStackControllerService: UiStackControllerService, private router: Router) {
  }

  ngOnInit(): void {
    this.uiStackControllerService.getStacksUsingGET1().subscribe(
      s => {
        return this.stacks = s;
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

}
