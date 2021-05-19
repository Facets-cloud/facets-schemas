import {Injectable} from '@angular/core';
import {Stack} from "../cc-api/models/stack";
import {AbstractCluster} from "../cc-api/models/abstract-cluster";

@Injectable({
  providedIn: 'root'
})
export class ClusterCreateHelperService {

  constructor() {
  }

  public loadClusterVarsFromStack(stackObj: Stack, dataSourceForSecrets, dataSourceForCommonVars) {
    Object.keys(stackObj.clusterVariablesMeta).forEach(key => {
      let element = stackObj.clusterVariablesMeta[key];
      if (element.secret) {
        dataSourceForSecrets.push({name: key, value: element.value, required: element.required});
      } else {
        dataSourceForCommonVars.push({name: key, value: element.value, required: element.required});
      }
    });
  }

  public loadClusterVarsFromCluster(cluster: AbstractCluster, dataSourceForSecrets, dataSourceForCommonVars, extraEnvVars) {
    Object.keys(cluster.commonEnvironmentVariables).forEach(element => {
      if (!extraEnvVars.includes(element)) {
        dataSourceForCommonVars.push({name: element, value: cluster.commonEnvironmentVariables[element]});
      }
    });

    Object.keys(cluster.secrets).forEach(element => {
      dataSourceForSecrets.push({name: element, value: cluster.secrets[element]});
    });
  }
}
