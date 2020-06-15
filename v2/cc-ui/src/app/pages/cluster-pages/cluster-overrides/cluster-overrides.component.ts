import {Component, OnInit} from '@angular/core';
import {UiCommonClusterControllerService} from '../../../cc-api/services/ui-common-cluster-controller.service';
import {ActivatedRoute} from '@angular/router';
import {OverrideObject} from '../../../cc-api/models/override-object';
import {concatAll, groupBy, mergeMap, toArray} from 'rxjs/internal/operators';
import {flatten} from '@angular/compiler';
import {flatMap} from 'tslint/lib/utils';
import {of} from 'rxjs/index';
import {NbMenuItem, NbMenuService} from '@nebular/theme';

@Component({
  selector: 'app-cluster-overrides',
  templateUrl: './cluster-overrides.component.html',
  styleUrls: ['./cluster-overrides.component.scss']
})
export class ClusterOverridesComponent implements OnInit {
  lookupMap = {};
  menuItems: NbMenuItem[] = [];

  currentSelection: OverrideObject;

  constructor(private clusterService: UiCommonClusterControllerService, private activatedRoute: ActivatedRoute,
              private menuService: NbMenuService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(p => {
      if (!p.clusterId) {
        console.log(p);
      }
      this.clusterService.getOverridesUsingGET1(p.clusterId).subscribe(
        res => {
          const groupMap = {};
          res.forEach(x => {
            const arr = groupMap[x.resourceType] ? groupMap[x.resourceType] : [];
            arr.push(x);
            groupMap[x.resourceType] = arr;
            this.lookupMap[x.resourceName] = x;
          });
          for (const appGroup in groupMap) {
            const applications = groupMap[appGroup].map(x => {
              this.currentSelection = this.currentSelection ? this.currentSelection : x;
              return {
                title: x.resourceName
              };
            });
            const menuItem: NbMenuItem = {
              title: appGroup.toUpperCase(),
              icon: 'list',
              expanded: true,
              children: applications,
            };
            this.menuItems.push(menuItem);
          }
        }
      );

      this.menuService.onItemClick().subscribe((event) => {
        this.selectApp(event.item.title);
      });
    });
  }

  selectApp(appName): void {
    this.currentSelection = this.lookupMap[appName];
  }

}
