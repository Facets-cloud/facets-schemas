/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { ClusterTask } from '../models/cluster-task';
import { AbstractCluster } from '../models/abstract-cluster';
import { SnapshotInfo } from '../models/snapshot-info';
import { OverrideObject } from '../models/override-object';
import { OverrideRequest } from '../models/override-request';
import { DeploymentLog } from '../models/deployment-log';
import { ResourceDetails } from '../models/resource-details';
import { SilenceAlarmRequest } from '../models/silence-alarm-request';

/**
 * Ui Common Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiCommonClusterControllerService extends __BaseService {
  static readonly disableClusterTaskUsingPOSTPath = '/cc-ui/v1/clusters/clusterTask/disable';
  static readonly enableClusterTaskUsingPOSTPath = '/cc-ui/v1/clusters/clusterTask/enable';
  static readonly getClusterCommonUsingGETPath = '/cc-ui/v1/clusters/{clusterId}';
  static readonly getAlertsUsingGETPath = '/cc-ui/v1/clusters/{clusterId}/alerts';
  static readonly getClusterTaskUsingGETPath = '/cc-ui/v1/clusters/{clusterId}/clusterTask';
  static readonly listSnapshotsUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}';
  static readonly createSnapshotUsingPOSTPath = '/cc-ui/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}';
  static readonly getPinnedSnapshotUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}/pinnedSnapshot';
  static readonly pinSnapshotUsingPOST1Path = '/cc-ui/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}/pinnedSnapshot';
  static readonly downLoadVagrantZipUsingGETPath = '/cc-ui/v1/clusters/{clusterId}/getVagrant';
  static readonly getKubeConfigUsingGETPath = '/cc-ui/v1/clusters/{clusterId}/kubeconfig';
  static readonly refreshKubeConfigUsingGETPath = '/cc-ui/v1/clusters/{clusterId}/kubeconfig/refresh';
  static readonly getOpenAlertsUsingGETPath = '/cc-ui/v1/clusters/{clusterId}/open-alerts';
  static readonly getOverridesUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/overrides';
  static readonly overrideSizingUsingPOST1Path = '/cc-ui/v1/clusters/{clusterId}/overrides';
  static readonly deleteOverridesUsingDELETEPath = '/cc-ui/v1/clusters/{clusterId}/overrides/{resourceType}/{resourceName}';
  static readonly refreshResourceUsingPOSTPath = '/cc-ui/v1/clusters/{clusterId}/refreshResource';
  static readonly resourceDetailsUsingGETPath = '/cc-ui/v1/clusters/{clusterId}/resourceDetails';
  static readonly silenceAlertsUsingPOSTPath = '/cc-ui/v1/clusters/{clusterId}/silence-alerts';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * disableClusterTask
   * @param taskId taskId
   * @return OK
   */
  disableClusterTaskUsingPOSTResponse(taskId?: string): __Observable<__StrictHttpResponse<ClusterTask>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    if (taskId != null) __params = __params.set('taskId', taskId.toString());
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/clusterTask/disable`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ClusterTask>;
      })
    );
  }
  /**
   * disableClusterTask
   * @param taskId taskId
   * @return OK
   */
  disableClusterTaskUsingPOST(taskId?: string): __Observable<ClusterTask> {
    return this.disableClusterTaskUsingPOSTResponse(taskId).pipe(
      __map(_r => _r.body as ClusterTask)
    );
  }

  /**
   * enableClusterTask
   * @param taskId taskId
   * @return OK
   */
  enableClusterTaskUsingPOSTResponse(taskId?: string): __Observable<__StrictHttpResponse<ClusterTask>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    if (taskId != null) __params = __params.set('taskId', taskId.toString());
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/clusterTask/enable`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ClusterTask>;
      })
    );
  }
  /**
   * enableClusterTask
   * @param taskId taskId
   * @return OK
   */
  enableClusterTaskUsingPOST(taskId?: string): __Observable<ClusterTask> {
    return this.enableClusterTaskUsingPOSTResponse(taskId).pipe(
      __map(_r => _r.body as ClusterTask)
    );
  }

  /**
   * getClusterCommon
   * @param clusterId clusterId
   * @return OK
   */
  getClusterCommonUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<AbstractCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<AbstractCluster>;
      })
    );
  }
  /**
   * getClusterCommon
   * @param clusterId clusterId
   * @return OK
   */
  getClusterCommonUsingGET(clusterId: string): __Observable<AbstractCluster> {
    return this.getClusterCommonUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as AbstractCluster)
    );
  }

  /**
   * getAlerts
   * @param clusterId clusterId
   * @return OK
   */
  getAlertsUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<{[key: string]: {}}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/alerts`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{[key: string]: {}}>;
      })
    );
  }
  /**
   * getAlerts
   * @param clusterId clusterId
   * @return OK
   */
  getAlertsUsingGET(clusterId: string): __Observable<{[key: string]: {}}> {
    return this.getAlertsUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as {[key: string]: {}})
    );
  }

  /**
   * getClusterTask
   * @param clusterId clusterId
   * @return OK
   */
  getClusterTaskUsingGETResponse(clusterId?: string): __Observable<__StrictHttpResponse<ClusterTask>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    if (clusterId != null) __params = __params.set('clusterId', clusterId.toString());
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/clusterTask`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ClusterTask>;
      })
    );
  }
  /**
   * getClusterTask
   * @param clusterId clusterId
   * @return OK
   */
  getClusterTaskUsingGET(clusterId?: string): __Observable<ClusterTask> {
    return this.getClusterTaskUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as ClusterTask)
    );
  }

  /**
   * listSnapshots
   * @param params The `UiCommonClusterControllerService.ListSnapshotsUsingGET1Params` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  listSnapshotsUsingGET1Response(params: UiCommonClusterControllerService.ListSnapshotsUsingGET1Params): __Observable<__StrictHttpResponse<Array<SnapshotInfo>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/dr/${encodeURIComponent(params.resourceType)}/snapshots/${encodeURIComponent(params.instanceName)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<SnapshotInfo>>;
      })
    );
  }
  /**
   * listSnapshots
   * @param params The `UiCommonClusterControllerService.ListSnapshotsUsingGET1Params` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  listSnapshotsUsingGET1(params: UiCommonClusterControllerService.ListSnapshotsUsingGET1Params): __Observable<Array<SnapshotInfo>> {
    return this.listSnapshotsUsingGET1Response(params).pipe(
      __map(_r => _r.body as Array<SnapshotInfo>)
    );
  }

  /**
   * createSnapshot
   * @param params The `UiCommonClusterControllerService.CreateSnapshotUsingPOSTParams` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  createSnapshotUsingPOSTResponse(params: UiCommonClusterControllerService.CreateSnapshotUsingPOSTParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/dr/${encodeURIComponent(params.resourceType)}/snapshots/${encodeURIComponent(params.instanceName)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'text'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return (_r as HttpResponse<any>).clone({ body: (_r as HttpResponse<any>).body === 'true' }) as __StrictHttpResponse<boolean>
      })
    );
  }
  /**
   * createSnapshot
   * @param params The `UiCommonClusterControllerService.CreateSnapshotUsingPOSTParams` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  createSnapshotUsingPOST(params: UiCommonClusterControllerService.CreateSnapshotUsingPOSTParams): __Observable<boolean> {
    return this.createSnapshotUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * getPinnedSnapshot
   * @param params The `UiCommonClusterControllerService.GetPinnedSnapshotUsingGET1Params` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getPinnedSnapshotUsingGET1Response(params: UiCommonClusterControllerService.GetPinnedSnapshotUsingGET1Params): __Observable<__StrictHttpResponse<SnapshotInfo>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/dr/${encodeURIComponent(params.resourceType)}/snapshots/${encodeURIComponent(params.instanceName)}/pinnedSnapshot`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<SnapshotInfo>;
      })
    );
  }
  /**
   * getPinnedSnapshot
   * @param params The `UiCommonClusterControllerService.GetPinnedSnapshotUsingGET1Params` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getPinnedSnapshotUsingGET1(params: UiCommonClusterControllerService.GetPinnedSnapshotUsingGET1Params): __Observable<SnapshotInfo> {
    return this.getPinnedSnapshotUsingGET1Response(params).pipe(
      __map(_r => _r.body as SnapshotInfo)
    );
  }

  /**
   * pinSnapshot
   * @param params The `UiCommonClusterControllerService.PinSnapshotUsingPOST1Params` containing the following parameters:
   *
   * - `snapshotInfo`: snapshotInfo
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  pinSnapshotUsingPOST1Response(params: UiCommonClusterControllerService.PinSnapshotUsingPOST1Params): __Observable<__StrictHttpResponse<SnapshotInfo>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.snapshotInfo;



    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/dr/${encodeURIComponent(params.resourceType)}/snapshots/${encodeURIComponent(params.instanceName)}/pinnedSnapshot`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<SnapshotInfo>;
      })
    );
  }
  /**
   * pinSnapshot
   * @param params The `UiCommonClusterControllerService.PinSnapshotUsingPOST1Params` containing the following parameters:
   *
   * - `snapshotInfo`: snapshotInfo
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  pinSnapshotUsingPOST1(params: UiCommonClusterControllerService.PinSnapshotUsingPOST1Params): __Observable<SnapshotInfo> {
    return this.pinSnapshotUsingPOST1Response(params).pipe(
      __map(_r => _r.body as SnapshotInfo)
    );
  }

  /**
   * downLoadVagrantZip
   * @param clusterId clusterId
   */
  downLoadVagrantZipUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<null>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/getVagrant`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<null>;
      })
    );
  }
  /**
   * downLoadVagrantZip
   * @param clusterId clusterId
   */
  downLoadVagrantZipUsingGET(clusterId: string): __Observable<null> {
    return this.downLoadVagrantZipUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as null)
    );
  }

  /**
   * getKubeConfig
   * @param clusterId clusterId
   * @return OK
   */
  getKubeConfigUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/kubeconfig`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'text'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<string>;
      })
    );
  }
  /**
   * getKubeConfig
   * @param clusterId clusterId
   * @return OK
   */
  getKubeConfigUsingGET(clusterId: string): __Observable<string> {
    return this.getKubeConfigUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * refreshKubeConfig
   * @param clusterId clusterId
   * @return OK
   */
  refreshKubeConfigUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/kubeconfig/refresh`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'text'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return (_r as HttpResponse<any>).clone({ body: (_r as HttpResponse<any>).body === 'true' }) as __StrictHttpResponse<boolean>
      })
    );
  }
  /**
   * refreshKubeConfig
   * @param clusterId clusterId
   * @return OK
   */
  refreshKubeConfigUsingGET(clusterId: string): __Observable<boolean> {
    return this.refreshKubeConfigUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * getOpenAlerts
   * @param clusterId clusterId
   * @return OK
   */
  getOpenAlertsUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<{[key: string]: {}}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/open-alerts`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{[key: string]: {}}>;
      })
    );
  }
  /**
   * getOpenAlerts
   * @param clusterId clusterId
   * @return OK
   */
  getOpenAlertsUsingGET(clusterId: string): __Observable<{[key: string]: {}}> {
    return this.getOpenAlertsUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as {[key: string]: {}})
    );
  }

  /**
   * getOverrides
   * @param clusterId clusterId
   * @return OK
   */
  getOverridesUsingGET1Response(clusterId: string): __Observable<__StrictHttpResponse<Array<OverrideObject>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/overrides`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<OverrideObject>>;
      })
    );
  }
  /**
   * getOverrides
   * @param clusterId clusterId
   * @return OK
   */
  getOverridesUsingGET1(clusterId: string): __Observable<Array<OverrideObject>> {
    return this.getOverridesUsingGET1Response(clusterId).pipe(
      __map(_r => _r.body as Array<OverrideObject>)
    );
  }

  /**
   * overrideSizing
   * @param params The `UiCommonClusterControllerService.OverrideSizingUsingPOST1Params` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  overrideSizingUsingPOST1Response(params: UiCommonClusterControllerService.OverrideSizingUsingPOST1Params): __Observable<__StrictHttpResponse<Array<OverrideObject>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/overrides`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<OverrideObject>>;
      })
    );
  }
  /**
   * overrideSizing
   * @param params The `UiCommonClusterControllerService.OverrideSizingUsingPOST1Params` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  overrideSizingUsingPOST1(params: UiCommonClusterControllerService.OverrideSizingUsingPOST1Params): __Observable<Array<OverrideObject>> {
    return this.overrideSizingUsingPOST1Response(params).pipe(
      __map(_r => _r.body as Array<OverrideObject>)
    );
  }

  /**
   * deleteOverrides
   * @param params The `UiCommonClusterControllerService.DeleteOverridesUsingDELETEParams` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `resourceName`: resourceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  deleteOverridesUsingDELETEResponse(params: UiCommonClusterControllerService.DeleteOverridesUsingDELETEParams): __Observable<__StrictHttpResponse<Array<OverrideObject>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/overrides/${encodeURIComponent(params.resourceType)}/${encodeURIComponent(params.resourceName)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<OverrideObject>>;
      })
    );
  }
  /**
   * deleteOverrides
   * @param params The `UiCommonClusterControllerService.DeleteOverridesUsingDELETEParams` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `resourceName`: resourceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  deleteOverridesUsingDELETE(params: UiCommonClusterControllerService.DeleteOverridesUsingDELETEParams): __Observable<Array<OverrideObject>> {
    return this.deleteOverridesUsingDELETEResponse(params).pipe(
      __map(_r => _r.body as Array<OverrideObject>)
    );
  }

  /**
   * refreshResource
   * @param clusterId clusterId
   * @return OK
   */
  refreshResourceUsingPOSTResponse(clusterId: string): __Observable<__StrictHttpResponse<DeploymentLog>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/refreshResource`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<DeploymentLog>;
      })
    );
  }
  /**
   * refreshResource
   * @param clusterId clusterId
   * @return OK
   */
  refreshResourceUsingPOST(clusterId: string): __Observable<DeploymentLog> {
    return this.refreshResourceUsingPOSTResponse(clusterId).pipe(
      __map(_r => _r.body as DeploymentLog)
    );
  }

  /**
   * resourceDetails
   * @param clusterId clusterId
   * @return OK
   */
  resourceDetailsUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<Array<ResourceDetails>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/resourceDetails`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ResourceDetails>>;
      })
    );
  }
  /**
   * resourceDetails
   * @param clusterId clusterId
   * @return OK
   */
  resourceDetailsUsingGET(clusterId: string): __Observable<Array<ResourceDetails>> {
    return this.resourceDetailsUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as Array<ResourceDetails>)
    );
  }

  /**
   * silenceAlerts
   * @param params The `UiCommonClusterControllerService.SilenceAlertsUsingPOSTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  silenceAlertsUsingPOSTResponse(params: UiCommonClusterControllerService.SilenceAlertsUsingPOSTParams): __Observable<__StrictHttpResponse<{[key: string]: {}}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/silence-alerts`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{[key: string]: {}}>;
      })
    );
  }
  /**
   * silenceAlerts
   * @param params The `UiCommonClusterControllerService.SilenceAlertsUsingPOSTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  silenceAlertsUsingPOST(params: UiCommonClusterControllerService.SilenceAlertsUsingPOSTParams): __Observable<{[key: string]: {}}> {
    return this.silenceAlertsUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as {[key: string]: {}})
    );
  }
}

module UiCommonClusterControllerService {

  /**
   * Parameters for listSnapshotsUsingGET1
   */
  export interface ListSnapshotsUsingGET1Params {

    /**
     * resourceType
     */
    resourceType: string;

    /**
     * instanceName
     */
    instanceName: string;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for createSnapshotUsingPOST
   */
  export interface CreateSnapshotUsingPOSTParams {

    /**
     * resourceType
     */
    resourceType: string;

    /**
     * instanceName
     */
    instanceName: string;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for getPinnedSnapshotUsingGET1
   */
  export interface GetPinnedSnapshotUsingGET1Params {

    /**
     * resourceType
     */
    resourceType: string;

    /**
     * instanceName
     */
    instanceName: string;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for pinSnapshotUsingPOST1
   */
  export interface PinSnapshotUsingPOST1Params {

    /**
     * snapshotInfo
     */
    snapshotInfo: SnapshotInfo;

    /**
     * resourceType
     */
    resourceType: string;

    /**
     * instanceName
     */
    instanceName: string;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for overrideSizingUsingPOST1
   */
  export interface OverrideSizingUsingPOST1Params {

    /**
     * request
     */
    request: Array<OverrideRequest>;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for deleteOverridesUsingDELETE
   */
  export interface DeleteOverridesUsingDELETEParams {

    /**
     * resourceType
     */
    resourceType: string;

    /**
     * resourceName
     */
    resourceName: string;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for silenceAlertsUsingPOST
   */
  export interface SilenceAlertsUsingPOSTParams {

    /**
     * request
     */
    request: SilenceAlarmRequest;

    /**
     * clusterId
     */
    clusterId: string;
  }
}

export { UiCommonClusterControllerService }
