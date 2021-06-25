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
 * Ui Azure Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiAzureClusterControllerService extends __BaseService {
  static readonly createAzureClusterUsingPOSTPath = '/cc-ui/v1/azure/clusters';
  static readonly getAzureClusterUsingGETPath = '/cc-ui/v1/azure/clusters/{clusterId}';
  static readonly updateAzureClusterUsingPUTPath = '/cc-ui/v1/azure/clusters/{clusterId}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * createAzureCluster
   * @param request request
   * @return OK
   */
  createAzureClusterUsingPOSTResponse(request: AzureClusterRequest): __Observable<__StrictHttpResponse<AzureCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = request;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/azure/clusters`,
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
   * createAzureCluster
   * @param request request
   * @return OK
   */
  createAzureClusterUsingPOST(request: AzureClusterRequest): __Observable<AzureCluster> {
    return this.createAzureClusterUsingPOSTResponse(request).pipe(
      __map(_r => _r.body as AzureCluster)
    );
  }

  /**
   * getAzureCluster
   * @param clusterId clusterId
   * @return OK
   */
  getAzureClusterUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<AzureCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/azure/clusters/${encodeURIComponent(clusterId)}`,
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
   * getAzureCluster
   * @param clusterId clusterId
   * @return OK
   */
  getAzureClusterUsingGET(clusterId: string): __Observable<AzureCluster> {
    return this.getAzureClusterUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as AzureCluster)
    );
  }

  /**
   * updateAzureCluster
   * @param params The `UiAzureClusterControllerService.UpdateAzureClusterUsingPUTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  updateAzureClusterUsingPUTResponse(params: UiAzureClusterControllerService.UpdateAzureClusterUsingPUTParams): __Observable<__StrictHttpResponse<AzureCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc-ui/v1/azure/clusters/${encodeURIComponent(params.clusterId)}`,
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
   * updateAzureCluster
   * @param params The `UiAzureClusterControllerService.UpdateAzureClusterUsingPUTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  updateAzureClusterUsingPUT(params: UiAzureClusterControllerService.UpdateAzureClusterUsingPUTParams): __Observable<AzureCluster> {
    return this.updateAzureClusterUsingPUTResponse(params).pipe(
      __map(_r => _r.body as AzureCluster)
    );
  }
}

module UiAzureClusterControllerService {

  /**
   * Parameters for updateAzureClusterUsingPUT
   */
  export interface UpdateAzureClusterUsingPUTParams {

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

export { UiAzureClusterControllerService }
