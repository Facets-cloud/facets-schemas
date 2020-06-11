import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
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
  clusters: AbstractCluster[];

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
    actions: false,
    hideSubHeader: true,
  };

  constructor(private route: ActivatedRoute, private uiStackControllerService: UiStackControllerService) {
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
          this.clusters = c;
        }
      );
    });
  }

}
