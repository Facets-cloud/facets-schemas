import { Component, OnInit, ViewChild } from '@angular/core';
import { UiCommonClusterControllerService } from '../../../cc-api/services/ui-common-cluster-controller.service';
import { ActivatedRoute } from '@angular/router';
import { OverrideObject } from '../../../cc-api/models/override-object';
import { NbMenuItem, NbMenuService, NbToastrService, NbDialogService } from '@nebular/theme';
import { JsonEditorComponent, JsonEditorOptions } from 'ang-jsoneditor';
import { UiStackControllerService, UiAwsClusterControllerService } from 'src/app/cc-api/services';
import { AwsClusterRequest, AbstractCluster } from 'src/app/cc-api/models';
import { PopupAppOverrideComponent } from './popup-app-override/popup-app-override.component';
import { DeleteOverrideDialogComponent } from './delete-override-dialog/delete-override-dialog.component';


@Component({
  selector: 'app-cluster-overrides',
  templateUrl: './cluster-overrides.component.html',
  styleUrls: ['./cluster-overrides.component.scss']
})
export class ClusterOverridesComponent implements OnInit {
  settings = {
    columns: {
      name: {
        title: 'Variable Name',
        filter: false,
        width: '50%',
        editable: false,
      },
      value: {
        title: 'Variable Value',
        filter: false,
        width: '50%',
        editor: { type: 'text' },
      }
    },
    noDataMessage: '',
    add: {
      addButtonContent: '&#10133;',
      createButtonContent: '&#10003;',
      cancelButtonContent: '&#10005;',
      confirmCreate: true,
    },
    delete: {
      deleteButtonContent: '&#9986;',
      confirmDelete: false,
    },
    edit: {
      editButtonContent: '&#9998;',
      saveButtonContent: '&#10003;',
      cancelButtonContent: '&#10005;',
      editConfirm: true,
      confirmSave: true,
    },
    pager: {
      display: true,
      perPage: 5,
    },
    actions: {
      edit: true,
      delete: true,
      position: 'right',
    },
  };

  lookupMap = {};
  menuItems: NbMenuItem[] = [];

  currentSelection: OverrideObject;
  editorOptions: JsonEditorOptions;
  enableSubmitForOverride = false;

  @ViewChild('editor', { static: false }) editor: JsonEditorComponent;

  private clusterId: any;

  constructor(
    private clusterService: UiCommonClusterControllerService,
    private activatedRoute: ActivatedRoute, private menuService: NbMenuService,
    private dialogService: NbDialogService) {
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
            this.lookupMap[x.resourceType + "-" + x.resourceName] = x;
          });
          for (const appGroup in groupMap) {
            const applications = groupMap[appGroup].map(x => {
              this.currentSelection = this.currentSelection ? this.currentSelection : x;
              return {
                title: x.resourceName,
                resourceType: x.resourceType
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
        this.selectApp(event.item.title, event.item["resourceType"]);
      });
    });

    this.search("");
  }

  selectApp(appName, resourceType): void {
    this.currentSelection = this.lookupMap[resourceType + '-' + appName];
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

  search(searchString) {
    if (searchString.length === 0) {
      this.menuItems.forEach(
        x => {
          x.expanded = false;
          x.children.forEach(y => y.hidden = false);
        },
      );
    } else {
      this.menuItems.forEach(
        x => x.children.forEach(
          y => {
            if (y.title.includes(searchString)) {
              x.expanded = true;
              y.hidden = false;
            } else {
              y.hidden = true;
            }
          },
        ),
      );
    }
  }

  addOverride() {
    this.dialogService.open(PopupAppOverrideComponent, { context: { clusterId: this.clusterId, existingOverrides: this.lookupMap } });
  }

  deleteOverride() {
    this.dialogService.open(DeleteOverrideDialogComponent).onClose.subscribe(proceed => {
      if (proceed) {
        this.clusterService.deleteOverridesUsingDELETE({
          clusterId: this.clusterId,
          resourceType: this.currentSelection.resourceType,
          resourceName: this.currentSelection.resourceName
        }).subscribe((deletedOverrides) => {
          location.reload();
        })
      }
    });
  }

  submitOverrides() {
    try {
      const newOverrides = this.editor.get();
      console.log('Submit Changes');
      this.currentSelection.overrides = newOverrides;
      this.clusterService.overrideSizingUsingPOST1({
        request: [this.currentSelection],
        clusterId: this.clusterId
      }).subscribe(r => {
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