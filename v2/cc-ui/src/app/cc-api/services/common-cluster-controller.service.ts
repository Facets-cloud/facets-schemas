/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { K8sCredentials } from '../models/k8s-credentials';
import { SnapshotInfo } from '../models/snapshot-info';
import { OverrideObject } from '../models/override-object';
import { OverrideRequest } from '../models/override-request';

/**
 * Common Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class CommonClusterControllerService extends __BaseService {
  static readonly addClusterK8sCredentialsUsingPOSTPath = '/cc/v1/clusters/{clusterId}/credentials';
  static readonly listSnapshotsUsingGETPath = '/cc/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}';
  static readonly getPinnedSnapshotUsingGETPath = '/cc/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}/pinnedSnapshot';
  static readonly pinSnapshotUsingPOSTPath = '/cc/v1/clusters/{clusterId}/dr/{resourceType}/snapshots/{instanceName}/pinnedSnapshot';
  static readonly getOverridesUsingGETPath = '/cc/v1/clusters/{clusterId}/overrides';
  static readonly overrideSizingUsingPOSTPath = '/cc/v1/clusters/{clusterId}/overrides';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * addClusterK8sCredentials
   * @param params The `CommonClusterControllerService.AddClusterK8sCredentialsUsingPOSTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  addClusterK8sCredentialsUsingPOSTResponse(params: CommonClusterControllerService.AddClusterK8sCredentialsUsingPOSTParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/credentials`,
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
   * addClusterK8sCredentials
   * @param params The `CommonClusterControllerService.AddClusterK8sCredentialsUsingPOSTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  addClusterK8sCredentialsUsingPOST(params: CommonClusterControllerService.AddClusterK8sCredentialsUsingPOSTParams): __Observable<boolean> {
    return this.addClusterK8sCredentialsUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * listSnapshots
   * @param params The `CommonClusterControllerService.ListSnapshotsUsingGETParams` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  listSnapshotsUsingGETResponse(params: CommonClusterControllerService.ListSnapshotsUsingGETParams): __Observable<__StrictHttpResponse<Array<SnapshotInfo>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/dr/${encodeURIComponent(params.resourceType)}/snapshots/${encodeURIComponent(params.instanceName)}`,
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
   * @param params The `CommonClusterControllerService.ListSnapshotsUsingGETParams` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  listSnapshotsUsingGET(params: CommonClusterControllerService.ListSnapshotsUsingGETParams): __Observable<Array<SnapshotInfo>> {
    return this.listSnapshotsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<SnapshotInfo>)
    );
  }

  /**
   * getPinnedSnapshot
   * @param params The `CommonClusterControllerService.GetPinnedSnapshotUsingGETParams` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getPinnedSnapshotUsingGETResponse(params: CommonClusterControllerService.GetPinnedSnapshotUsingGETParams): __Observable<__StrictHttpResponse<SnapshotInfo>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/dr/${encodeURIComponent(params.resourceType)}/snapshots/${encodeURIComponent(params.instanceName)}/pinnedSnapshot`,
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
   * @param params The `CommonClusterControllerService.GetPinnedSnapshotUsingGETParams` containing the following parameters:
   *
   * - `resourceType`: resourceType
   *
   * - `instanceName`: instanceName
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getPinnedSnapshotUsingGET(params: CommonClusterControllerService.GetPinnedSnapshotUsingGETParams): __Observable<SnapshotInfo> {
    return this.getPinnedSnapshotUsingGETResponse(params).pipe(
      __map(_r => _r.body as SnapshotInfo)
    );
  }

  /**
   * pinSnapshot
   * @param params The `CommonClusterControllerService.PinSnapshotUsingPOSTParams` containing the following parameters:
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
  pinSnapshotUsingPOSTResponse(params: CommonClusterControllerService.PinSnapshotUsingPOSTParams): __Observable<__StrictHttpResponse<SnapshotInfo>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.snapshotInfo;



    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/dr/${encodeURIComponent(params.resourceType)}/snapshots/${encodeURIComponent(params.instanceName)}/pinnedSnapshot`,
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
   * @param params The `CommonClusterControllerService.PinSnapshotUsingPOSTParams` containing the following parameters:
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
  pinSnapshotUsingPOST(params: CommonClusterControllerService.PinSnapshotUsingPOSTParams): __Observable<SnapshotInfo> {
    return this.pinSnapshotUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as SnapshotInfo)
    );
  }

  /**
   * getOverrides
   * @param clusterId clusterId
   * @return OK
   */
  getOverridesUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<Array<OverrideObject>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(clusterId)}/overrides`,
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
  getOverridesUsingGET(clusterId: string): __Observable<Array<OverrideObject>> {
    return this.getOverridesUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as Array<OverrideObject>)
    );
  }

  /**
   * overrideSizing
   * @param params The `CommonClusterControllerService.OverrideSizingUsingPOSTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  overrideSizingUsingPOSTResponse(params: CommonClusterControllerService.OverrideSizingUsingPOSTParams): __Observable<__StrictHttpResponse<Array<OverrideObject>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/overrides`,
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
   * @param params The `CommonClusterControllerService.OverrideSizingUsingPOSTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  overrideSizingUsingPOST(params: CommonClusterControllerService.OverrideSizingUsingPOSTParams): __Observable<Array<OverrideObject>> {
    return this.overrideSizingUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Array<OverrideObject>)
    );
  }
}

module CommonClusterControllerService {

  /**
   * Parameters for addClusterK8sCredentialsUsingPOST
   */
  export interface AddClusterK8sCredentialsUsingPOSTParams {

    /**
     * request
     */
    request: K8sCredentials;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for listSnapshotsUsingGET
   */
  export interface ListSnapshotsUsingGETParams {

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
   * Parameters for getPinnedSnapshotUsingGET
   */
  export interface GetPinnedSnapshotUsingGETParams {

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
   * Parameters for pinSnapshotUsingPOST
   */
  export interface PinSnapshotUsingPOSTParams {

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
   * Parameters for overrideSizingUsingPOST
   */
  export interface OverrideSizingUsingPOSTParams {

    /**
     * request
     */
    request: Array<OverrideRequest>;

    /**
     * clusterId
     */
    clusterId: string;
  }
}

export { CommonClusterControllerService }
