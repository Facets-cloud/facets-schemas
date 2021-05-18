/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { LocalCluster } from '../models/local-cluster';
import { LocalClusterRequest } from '../models/local-cluster-request';

/**
 * Ui Local Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiLocalClusterControllerService extends __BaseService {
  static readonly createClusterUsingPOST3Path = '/cc-ui/v1/local/clusters';
  static readonly getClusterUsingGET3Path = '/cc-ui/v1/local/clusters/{clusterId}';
  static readonly updateClusterUsingPUT3Path = '/cc-ui/v1/local/clusters/{clusterId}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * createCluster
   * @param request request
   * @return OK
   */
  createClusterUsingPOST3Response(request: LocalClusterRequest): __Observable<__StrictHttpResponse<LocalCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = request;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/local/clusters`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<LocalCluster>;
      })
    );
  }
  /**
   * createCluster
   * @param request request
   * @return OK
   */
  createClusterUsingPOST3(request: LocalClusterRequest): __Observable<LocalCluster> {
    return this.createClusterUsingPOST3Response(request).pipe(
      __map(_r => _r.body as LocalCluster)
    );
  }

  /**
   * getCluster
   * @param clusterId clusterId
   * @return OK
   */
  getClusterUsingGET3Response(clusterId: string): __Observable<__StrictHttpResponse<LocalCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/local/clusters/${encodeURIComponent(clusterId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<LocalCluster>;
      })
    );
  }
  /**
   * getCluster
   * @param clusterId clusterId
   * @return OK
   */
  getClusterUsingGET3(clusterId: string): __Observable<LocalCluster> {
    return this.getClusterUsingGET3Response(clusterId).pipe(
      __map(_r => _r.body as LocalCluster)
    );
  }

  /**
   * updateCluster
   * @param params The `UiLocalClusterControllerService.UpdateClusterUsingPUT3Params` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  updateClusterUsingPUT3Response(params: UiLocalClusterControllerService.UpdateClusterUsingPUT3Params): __Observable<__StrictHttpResponse<LocalCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc-ui/v1/local/clusters/${encodeURIComponent(params.clusterId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<LocalCluster>;
      })
    );
  }
  /**
   * updateCluster
   * @param params The `UiLocalClusterControllerService.UpdateClusterUsingPUT3Params` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  updateClusterUsingPUT3(params: UiLocalClusterControllerService.UpdateClusterUsingPUT3Params): __Observable<LocalCluster> {
    return this.updateClusterUsingPUT3Response(params).pipe(
      __map(_r => _r.body as LocalCluster)
    );
  }
}

module UiLocalClusterControllerService {

  /**
   * Parameters for updateClusterUsingPUT3
   */
  export interface UpdateClusterUsingPUT3Params {

    /**
     * request
     */
    request: LocalClusterRequest;

    /**
     * clusterId
     */
    clusterId: string;
  }
}

export { UiLocalClusterControllerService }
