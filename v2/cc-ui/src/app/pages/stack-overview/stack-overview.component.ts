import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {Stack} from '../../cc-api/models/stack';
import {AbstractCluster} from '../../cc-api/models/abstract-cluster';

@Component({
  selector: 'app-stack-overview',
  templateUrl: './stack-overview.component.html',
  styleUrls: ['./stack-overview.component.scss']
})
export class StackOverviewComponent implements OnInit {
  stack: Stack;
  tableData: any[];


  clusterSettings = {
    columns: {
      id: {
        title: 'ClusterId',
      },
      name: {
        title: 'Cluster Name',
      },
      cloud: {
        title: 'Cloud Provider',
      },
      releaseStream: {
        title: 'Release Stream',
      },
      stackName: {
        title: 'Stack Name',
      },
      tz: {
        title: 'Time Zone',
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

  constructor(private route: ActivatedRoute, private uiStackControllerService: UiStackControllerService, private router: Router) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (!p.stackName) {
        // redirect to homepage
      }
      this.uiStackControllerService.getStackUsingGET(p.stackName).subscribe(
        s => {
          this.stack = s;
        }
      );
      this.uiStackControllerService.getClustersUsingGET1(p.stackName).subscribe(
        c => {
          this.tableData = c;
        }
      );
    });
  }

  gotoPage(x): void {
    if (x.action === 'View') {
      const clusterId = x.data.id;
      console.log('Navigate to ' + clusterId);
      this.router.navigate(['/capc/', x.data.stackName, 'cluster', clusterId]);
    }
  }
}
