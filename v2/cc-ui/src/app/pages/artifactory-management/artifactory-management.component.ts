import {Component, OnInit} from '@angular/core';
import {UiArtifactoryControllerService} from "../../cc-api/services/ui-artifactory-controller.service";
import {Artifactory} from "../../cc-api/models/artifactory";
import {NbDialogService} from "@nebular/theme";
import {ECRArtifactory} from "../../cc-api/models/ecrartifactory";

@Component({
  selector: 'app-artifactory-management',
  templateUrl: './artifactory-management.component.html',
  styleUrls: ['./artifactory-management.component.scss']
})
export class ArtifactoryManagementComponent implements OnInit {
  artifactories: Array<Artifactory>;
  tableSettings: any = {
    columns: {
      id: {
        title: 'Id',
        type: 'text',
        filter: false,
      },
      name: {
        title: 'Name',
        filter: true,
        type: 'text',
      },
      uri: {
        title: 'URL',
        filter: true,
        type: 'text',
      },
      awsRegion: {
        title: 'AWS Region',
        filter: true,
        type: 'text',
      },
      awsAccountId: {
        title: 'AWS AccountId',
        filter: true,
        type: 'text',
      },
    },
    noDataMessage: 'No Artifactory created yet! Get started',
    add: {
      addButtonContent: '<i class="eva-plus-outline eva"></i>',
    },
    pager: {
      display: false,
      perPage: 15,
    },
    mode: "external",
    actions: {
      add: false,
      position: 'right',
      delete: false,
      custom: [{name: 'Edit', title: '<i class="eva-edit-2-outline eva"></i>', type: 'html'}]
    },
  };

  artifactoryTemp: ECRArtifactory;
  error: any;

  constructor(private artifactoryControllerService: UiArtifactoryControllerService, private dialogService: NbDialogService,) {
  }

  ngOnInit(): void {
    this.artifactoryControllerService.getAllArtifactoriesUsingGET1().subscribe(
      response => {
        this.artifactories = response;
      },
      error => {

      }
    )
  }

  openCreatePopup(dialogRef) {
    this.error = undefined;
    this.artifactoryTemp = new class implements ECRArtifactory {
      awsAccountId: string;
      awsKey: string;
      awsRegion: string;
      awsSecret: string;
      id: string;
      name: string;
      uri: string;
    }
    this.dialogService.open(dialogRef, {context: {edit: false}});
  }

  openEditPopup(dialogRef, $event: any) {
    this.error = undefined;
    this.artifactoryTemp = new class implements ECRArtifactory {
      awsAccountId: string;
      awsKey: string;
      awsRegion: string;
      awsSecret: string;
      id: string;
      name: string;
      uri: string;
    }
    this.artifactoryTemp.id = $event.data.id;
    this.artifactoryTemp.name = $event.data.name;
    this.artifactoryTemp.uri = $event.data.uri;
    this.artifactoryTemp.awsRegion = $event.data.awsRegion;
    this.artifactoryTemp.awsKey = $event.data.awsKey;
    this.artifactoryTemp.awsAccountId = $event.data.awsAccountId;
    this.dialogService.open(dialogRef, {context: {edit: true}});
  }

  editArtifactoryCall(ref) {
    this.artifactoryControllerService.updateECRArtifactoryUsingPOST({
      artifactoryId: this.artifactoryTemp.id,
      ecrArtifactory: this.artifactoryTemp
    }).subscribe(
      response => {
        ref.close;
        this.artifactoryControllerService.getAllArtifactoriesUsingGET1().subscribe(
          response => {
            this.artifactories = response;
          }
        )
      },
      error => {

        this.error = error.error.message || error.message
      }
    )
  }

  createArtifactoryCall(ref) {
    this.artifactoryControllerService.createECRArtifactoryUsingPOST1(this.artifactoryTemp).subscribe(
      response => {
        ref.close;
        this.artifactoryControllerService.getAllArtifactoriesUsingGET1().subscribe(
          response => {
            this.artifactories = response;
          }
        )
      },
      error => {

        this.error = error.error.message || error.message
      }
    )
  }
}
