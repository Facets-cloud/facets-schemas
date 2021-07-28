import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { AbstractCluster, ProvidedCloudResource, ProvidedResources, Resource, Stack } from 'src/app/cc-api/models';
import { UiAwsClusterControllerService, UiCommonClusterControllerService, UiStackControllerService } from 'src/app/cc-api/services';
import { ProvidedResourcesModule } from './provided-resources.module';

@Component({
  selector: 'app-provided-resources',
  templateUrl: './provided-resources.component.html',
  styleUrls: ['./provided-resources.component.scss']
})
export class ProvidedResourcesComponent implements OnInit {

  stack: Stack;
  clusterId: string;
  providedResources: ProvidedResources = {};

  constructor(private uiCommonClusterControllerService: UiCommonClusterControllerService,
    private uiStackControllerService: UiStackControllerService,
    private activatedRoute: ActivatedRoute, private toastrService: NbToastrService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      p => {
        this.clusterId = p.clusterId
        this.providedResources.clusterId = this.clusterId
        if(p.stackName) {
          this.uiStackControllerService.getStackUsingGET(p.stackName).subscribe(
            x => this.getProvidedResources(x, p.clusterId),
          )
        }
      }
    );
  }

  private getProvidedResources(x: Stack, clusterId: string) {
    this.stack = x;
    this.uiCommonClusterControllerService.getProvidedResourcesUsingGET(clusterId).subscribe(
      currentProvidedResources => {
        this.providedResources.providedCloudResources = this.getProvidedCloudResources(x.providedResources, currentProvidedResources.providedCloudResources)
      }
    );
  }

  getResourceType(x: string) {
    if(x === "s3") {
      return "CLOUD";
    } else {
      return "AUTHENTICATED_SERVICE"
    }
  }

  save() {
    this.uiCommonClusterControllerService.upsertProvidedResourcesUsingPOST({
      clusterId: this.clusterId,
      providedResources: this.providedResources
    }).subscribe(
      (x: ProvidedResources) => this.toastrService.success("Provided resource details updated", "Success"),
      err => this.toastrService.danger("Provided resource details update failed", "Failure"),
    );
  }

  getProvidedCloudResources(providedResources: Resource[], currentProvidedCloudResources: ProvidedCloudResource[]): ProvidedCloudResource[] {
    console.log(currentProvidedCloudResources)
    var providedCloudResourceDefs: Resource[] = providedResources.filter(x => this.getResourceType(x.resourceType) === "CLOUD");
    var providedCloudResources:  ProvidedCloudResource[] = []
    currentProvidedCloudResources.forEach(x => providedCloudResources.push(x));
    providedCloudResourceDefs.forEach(
      x => {
        var absent: boolean = currentProvidedCloudResources.filter(c =>
          c.resourceType === x.resourceType && c.resourceName === x.resourceName)
          .length == 0;
        if (absent) {
          providedCloudResources.push({
            resourceType: x.resourceType,
            resourceName: x.resourceName
          })
        }
      }
    )
    return providedCloudResources;
  }

}
