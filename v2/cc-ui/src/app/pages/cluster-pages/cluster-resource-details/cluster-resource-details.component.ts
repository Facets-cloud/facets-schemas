import { Component, Input, OnInit } from '@angular/core';
import { from } from 'rxjs';
import {UiCommonClusterControllerService} from 'src/app/cc-api/services';
import {LocalDataSource} from 'ng2-smart-table';
import { ActivatedRoute } from '@angular/router';
import { ResourceDetails } from 'src/app/cc-api/models';

@Component({
  selector: 'app-cluster-resource-details',
  templateUrl: './cluster-resource-details.component.html',
  styleUrls: ['./cluster-resource-details.component.scss']
})
export class ClusterResourceDetailsComponent implements OnInit {

  resourceDetails: ResourceDetails[] = null;
  resourceTypes: Set<string> = new Set<string>();
  clusterDetailsMap: Map<string, LocalDataSource> = new Map<string, LocalDataSource>();

  resourceDetailsSettings = {
    hideSubHeader: true,
    columns: {
      name: {
        title: 'Name',
        filter: false,
        width: '33%',
        editable: false,
      },
      key: {
        title: 'Key',
        filter: false,
        width: '33%'
      },
      value: {
        title: 'Value',
        filter: false,
        width: '33%',
        editable: false,
      }
    },
    actions: {
      add: false,
      edit: false,
      delete: false,
      position: 'right',
    },
  };

  constructor(private uiCommonClusterControllerService: UiCommonClusterControllerService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    let clusterId = ''
    this.activatedRoute.params.subscribe(
      p => {
        if(p.clusterId){
          clusterId = p.clusterId;
          this.uiCommonClusterControllerService.resourceDetailsUsingGET(clusterId).subscribe(
            t => {
              if(t.length == 0){
                this.resourceDetails = null;
              }else{
                this.resourceDetails = t;
                this.updateResourceTypes();
                this.updateTableSourceWithResourceDetails();
              }
            },
            error => {
              this.resourceDetails = null;
              this.resourceTypes = new Set<string>();
              this.clusterDetailsMap = new Map<string, LocalDataSource>();
            }
          );
        }
      }
    );

  }

  updateResourceTypes(){
    this.resourceDetails.forEach(element => {
      if(element.resourceType != null){
        this.resourceTypes.add(element.resourceType);
      }
    });
  }

  updateTableSourceWithResourceDetails() {
    this.resourceTypes.forEach(element => {
      let dataSource = new LocalDataSource();
      this.resourceDetails.filter((x) => x.resourceType === element).forEach(record => {
        dataSource.add({name: record.name, key: record.key, value: record.value});
      });
      this.clusterDetailsMap.set(element, dataSource);
      console.log("cluster details map is " + this.clusterDetailsMap);
    });
  }

}
