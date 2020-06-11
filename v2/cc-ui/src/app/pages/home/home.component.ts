import {Component, OnInit} from '@angular/core';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {Stack} from '../../cc-api/models/stack';

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
      },
      custom : {
        type: 'html',
        title: 'Actions',
      }
    },
    actions: false,
    hideSubHeader: true,
  };

  constructor(private uiStackControllerService: UiStackControllerService) {
  }

  ngOnInit(): void {
    this.uiStackControllerService.getStacksUsingGET1().subscribe(
      s => {
        return this.stacks = s;
      }
    );
  }

}
