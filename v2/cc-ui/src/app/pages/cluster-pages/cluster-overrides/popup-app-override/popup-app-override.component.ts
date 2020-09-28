import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { JsonEditorOptions, JsonEditorComponent } from 'ang-jsoneditor';
import { OverrideObject } from 'src/app/cc-api/models';
import { NbToastrService } from '@nebular/theme';
import { UiCommonClusterControllerService } from 'src/app/cc-api/services';

@Component({
  selector: 'app-popup-app-override',
  templateUrl: './popup-app-override.component.html',
  styleUrls: ['./popup-app-override.component.scss']
})
export class PopupAppOverrideComponent implements OnInit {

  enableSubmitForNewOverrides = false;
  newOverrideEditorOptions: JsonEditorOptions;
  newOverrideModuleType: string = "";
  newOverrideInstanceName: string = "";
  newOverrideCurrentSelection: OverrideObject = {};

  @ViewChild('editorNewOverride', { static: false }) editorNewOverride: JsonEditorComponent;

  @Input() clusterId: string;
  @Input() existingOverrides: {};

  constructor(
    private toastrService: NbToastrService,
    private clusterService: UiCommonClusterControllerService) { }

  ngOnInit(): void {
    this.newOverrideEditorOptions = new JsonEditorOptions();
    this.newOverrideEditorOptions.modes = ['code', 'tree', 'view']; // set code mode for new override
    this.newOverrideEditorOptions.mode = 'code'; // set code mode for new override
  }

  searchExistingOerride() {
    let overrides = this.existingOverrides;
    let instance = overrides[this.newOverrideModuleType + '-' + this.newOverrideInstanceName];

    if (!instance) {
      this.newOverrideCurrentSelection = {};
      return;
    }

    if (instance["resourceType"] != this.newOverrideModuleType) {
      this.newOverrideCurrentSelection = {};
      return;
    }

    this.newOverrideCurrentSelection = instance;
    this.editNewOverrides();
  }

  isEmptyObject(obj: Object) {
    return (obj && (Object.keys(obj).length === 0));
  }

  areValidNewOverrides() {
    if (!this.newOverrideModuleType || !this.newOverrideInstanceName) {
      return false;
    }

    const newOverrides = this.editorNewOverride;
    if (!newOverrides.isValidJson() || this.isEmptyObject(newOverrides.get())) {
      return false;
    }

    return true;
  }

  editNewOverrides() {
    if (!this.areValidNewOverrides()) {
      this.enableSubmitForNewOverrides = false;
      return;
    }

    this.enableSubmitForNewOverrides = true;
  }

  resetNewOverrides() {
    this.enableSubmitForNewOverrides = false;
    this.newOverrideCurrentSelection = {};
    this.newOverrideCurrentSelection.overrides = {};
  }

  submitNewOverrides() {
    try {
      if (!this.areValidNewOverrides()) {
        this.toastrService.danger("One or more override fields are missing", "Error");
        return;
      }

      const newOverrides = this.editorNewOverride.get();
      console.log('Submit Changes');

      this.newOverrideCurrentSelection.resourceType = this.newOverrideModuleType;
      this.newOverrideCurrentSelection.resourceName = this.newOverrideInstanceName;
      this.newOverrideCurrentSelection.overrides = newOverrides;

      this.clusterService.overrideSizingUsingPOST1({
        request: [this.newOverrideCurrentSelection],
        clusterId: this.clusterId
      }).subscribe(
        r => {
          console.log(r);
          this.toastrService.success("New Overrides Added", "Success");
          location.reload();
        }
      );
    } catch {
      console.log('Invalid Json');
      this.toastrService.danger("Invalid configuration for new overrides", "Error");
    } finally {
      this.resetNewOverrides();
    }
  }

}
