import {Component, OnInit, ViewChild} from '@angular/core';
import {UiCommonClusterControllerService} from '../../../cc-api/services/ui-common-cluster-controller.service';
import {ActivatedRoute} from '@angular/router';
import {OverrideObject} from '../../../cc-api/models/override-object';
import {concatAll, groupBy, mergeMap, toArray} from 'rxjs/internal/operators';
import {flatten} from '@angular/compiler';
import {flatMap} from 'tslint/lib/utils';
import {of} from 'rxjs/index';
import {NbGlobalPosition, NbMenuItem, NbMenuService, NbToastrService} from '@nebular/theme';
import {JsonEditorComponent, JsonEditorOptions} from 'ang-jsoneditor';


@Component({
  selector: 'app-cluster-overrides',
  templateUrl: './cluster-overrides.component.html',
  styleUrls: ['./cluster-overrides.component.scss']
})
export class ClusterOverridesComponent implements OnInit {
  lookupMap = {};
  menuItems: NbMenuItem[] = [];

  currentSelection: OverrideObject;
  editorOptions: JsonEditorOptions;
  enableSubmitForOverride = false;
  @ViewChild('editor', {static: false}) editor: JsonEditorComponent;
  private clusterId: any;


  constructor(private clusterService: UiCommonClusterControllerService, private activatedRoute: ActivatedRoute,
              private menuService: NbMenuService) {
  }

  ngOnInit(): void {
    this.editorOptions = new JsonEditorOptions();
    this.editorOptions.modes = ['code', 'tree', 'view']; // set all allowed modes
    this.activatedRoute.params.subscribe(p => {
      if (!p.clusterId) {
        console.log(p);
      }
      this.clusterId = p.clusterId;
      this.clusterService.getOverridesUsingGET1(p.clusterId).subscribe(
        res => {
          this.menuItems = [];
          this.lookupMap = {};
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

  editOverrides() {
    try {
      const newOverrides = this.editor.get();
      console.log(newOverrides);
      const original = this.currentSelection.overrides ? this.currentSelection.overrides : {};
      this.enableSubmitForOverride = JSON.stringify(newOverrides) !== JSON.stringify(original);
      console.log('HasChanged = ' + this.enableSubmitForOverride);
    } catch {
      this.enableSubmitForOverride = false;
      console.log('Invalid Json');
    }
  }

  submitOverrides() {
    try {
      const newOverrides = this.editor.get();
      console.log('Submit Changes');
      this.currentSelection.overrides = newOverrides;
      this.clusterService.overrideSizingUsingPOST1({
        request: [this.currentSelection],
        clusterId: this.clusterId
      }).subscribe(
        r => {
          console.log(r);
          this.enableSubmitForOverride = false;
        }
      );
    } catch {
      this.enableSubmitForOverride = false;
      console.log('Invalid Json');
    }
  }
}
