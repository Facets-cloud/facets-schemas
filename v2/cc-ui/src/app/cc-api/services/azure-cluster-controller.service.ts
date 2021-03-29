/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { AzureCluster } from '../models/azure-cluster';
import { AzureClusterRequest } from '../models/azure-cluster-request';

/**
 * Azure Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class AzureClusterControllerService extends __BaseService {
  static readonly createClusterUsingPOST1Path = '/cc/v1/azure/clusters';
  static readonly getClusterUsingGET1Path = '/cc/v1/azure/clusters/{clusterId}';
  static readonly updateClusterUsingPUT1Path = '/cc/v1/azure/clusters/{clusterId}';

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
  createClusterUsingPOST1Response(request: AzureClusterRequest): __Observable<__StrictHttpResponse<AzureCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = request;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/azure/clusters`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<AzureCluster>;
      })
    );
  }
  /**
   * createCluster
   * @param request request
   * @return OK
   */
  createClusterUsingPOST1(request: AzureClusterRequest): __Observable<AzureCluster> {
    return this.createClusterUsingPOST1Response(request).pipe(
      __map(_r => _r.body as AzureCluster)
    );
  }

  /**
   * getCluster
   * @param clusterId clusterId
   * @return OK
   */
  getClusterUsingGET1Response(clusterId: string): __Observable<__StrictHttpResponse<AzureCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/azure/clusters/${encodeURIComponent(clusterId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<AzureCluster>;
      })
    );
  }
  /**
   * getCluster
   * @param clusterId clusterId
   * @return OK
   */
  getClusterUsingGET1(clusterId: string): __Observable<AzureCluster> {
    return this.getClusterUsingGET1Response(clusterId).pipe(
      __map(_r => _r.body as AzureCluster)
    );
  }

  /**
   * updateCluster
   * @param params The `AzureClusterControllerService.UpdateClusterUsingPUT1Params` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  updateClusterUsingPUT1Response(params: AzureClusterControllerService.UpdateClusterUsingPUT1Params): __Observable<__StrictHttpResponse<AzureCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc/v1/azure/clusters/${encodeURIComponent(params.clusterId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<AzureCluster>;
      })
    );
  }
  /**
   * updateCluster
   * @param params The `AzureClusterControllerService.UpdateClusterUsingPUT1Params` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  updateClusterUsingPUT1(params: AzureClusterControllerService.UpdateClusterUsingPUT1Params): __Observable<AzureCluster> {
    return this.updateClusterUsingPUT1Response(params).pipe(
      __map(_r => _r.body as AzureCluster)
    );
  }
}

module AzureClusterControllerService {

  /**
   * Parameters for updateClusterUsingPUT1
   */
  export interface UpdateClusterUsingPUT1Params {

    /**
     * request
     */
    request: AzureClusterRequest;

    /**
     * clusterId
     */
    clusterId: string;
  }
}

export { AzureClusterControllerService }
