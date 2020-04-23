/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { AwsCluster } from '../models/aws-cluster';
import { AwsClusterRequest } from '../models/aws-cluster-request';

/**
 * Aws Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class AwsClusterControllerService extends __BaseService {
  static readonly createClusterUsingPOSTPath = '/cc/v1/aws/clusters';
  static readonly getClusterUsingGETPath = '/cc/v1/aws/clusters/{clusterId}';
  static readonly createClusterUsingPUTPath = '/cc/v1/aws/clusters/{clusterId}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @param request request
   * @return OK
   */
  createClusterUsingPOSTResponse(request: AwsClusterRequest): __Observable<__StrictHttpResponse<AwsCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = request;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/aws/clusters`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<AwsCluster>;
      })
    );
  }
  /**
   * @param request request
   * @return OK
   */
  createClusterUsingPOST(request: AwsClusterRequest): __Observable<AwsCluster> {
    return this.createClusterUsingPOSTResponse(request).pipe(
      __map(_r => _r.body as AwsCluster)
    );
  }

  /**
   * @param clusterId clusterId
   * @return OK
   */
  getClusterUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<AwsCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/aws/clusters/${clusterId}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<AwsCluster>;
      })
    );
  }
  /**
   * @param clusterId clusterId
   * @return OK
   */
  getClusterUsingGET(clusterId: string): __Observable<AwsCluster> {
    return this.getClusterUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as AwsCluster)
    );
  }

  /**
   * @param params The `AwsClusterControllerService.CreateClusterUsingPUTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  createClusterUsingPUTResponse(params: AwsClusterControllerService.CreateClusterUsingPUTParams): __Observable<__StrictHttpResponse<AwsCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc/v1/aws/clusters/${params.clusterId}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<AwsCluster>;
      })
    );
  }
  /**
   * @param params The `AwsClusterControllerService.CreateClusterUsingPUTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  createClusterUsingPUT(params: AwsClusterControllerService.CreateClusterUsingPUTParams): __Observable<AwsCluster> {
    return this.createClusterUsingPUTResponse(params).pipe(
      __map(_r => _r.body as AwsCluster)
    );
  }
}

module AwsClusterControllerService {

  /**
   * Parameters for createClusterUsingPUT
   */
  export interface CreateClusterUsingPUTParams {

    /**
     * request
     */
    request: AwsClusterRequest;

    /**
     * clusterId
     */
    clusterId: string;
  }
}

export { AwsClusterControllerService }
