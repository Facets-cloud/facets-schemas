/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { SnapshotInfo } from '../models/snapshot-info';
import { OverrideObject } from '../models/override-object';
import { OverrideRequest } from '../models/override-request';

/**
 * Ui Common Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiCommonClusterControllerService extends __BaseService {
  static readonly listSnapshotsUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}';
  static readonly createSnapshotUsingPOSTPath = '/cc-ui/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}';
  static readonly getPinnedSnapshotUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}/pinnedSnapshot';
  static readonly pinSnapshotUsingPOST1Path = '/cc-ui/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}/pinnedSnapshot';
  static readonly getOverridesUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/overrides';
  static readonly overrideSizingUsingPOST1Path = '/cc-ui/v1/clusters/{clusterId}/overrides';
  static readonly deleteOverridesUsingDELETEPath = '/cc-ui/v1/clusters/{clusterId}/overrides/{resourceType}/{resourceName}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
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
}

export { UiCommonClusterControllerService }
