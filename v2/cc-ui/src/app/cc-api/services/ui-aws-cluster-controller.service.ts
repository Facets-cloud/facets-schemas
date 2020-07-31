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
 * Ui Aws Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiAwsClusterControllerService extends __BaseService {
  static readonly createClusterUsingPOST1Path = '/cc-ui/v1/aws/clusters';
  static readonly getClusterUsingGET1Path = '/cc-ui/v1/aws/clusters/{clusterId}';
  static readonly updateClusterUsingPUT1Path = '/cc-ui/v1/aws/clusters/{clusterId}';

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
  createClusterUsingPOST1Response(request: AwsClusterRequest): __Observable<__StrictHttpResponse<AwsCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = request;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/aws/clusters`,
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
   * createCluster
   * @param request request
   * @return OK
   */
  createClusterUsingPOST1(request: AwsClusterRequest): __Observable<AwsCluster> {
    return this.createClusterUsingPOST1Response(request).pipe(
      __map(_r => _r.body as AwsCluster)
    );
  }

  /**
   * getCluster
   * @param clusterId clusterId
   * @return OK
   */
  getClusterUsingGET1Response(clusterId: string): __Observable<__StrictHttpResponse<AwsCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/aws/clusters/${encodeURIComponent(clusterId)}`,
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
   * getCluster
   * @param clusterId clusterId
   * @return OK
   */
  getClusterUsingGET1(clusterId: string): __Observable<AwsCluster> {
    return this.getClusterUsingGET1Response(clusterId).pipe(
      __map(_r => _r.body as AwsCluster)
    );
  }

  /**
   * updateCluster
   * @param params The `UiAwsClusterControllerService.UpdateClusterUsingPUT1Params` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  updateClusterUsingPUT1Response(params: UiAwsClusterControllerService.UpdateClusterUsingPUT1Params): __Observable<__StrictHttpResponse<AwsCluster>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc-ui/v1/aws/clusters/${encodeURIComponent(params.clusterId)}`,
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
   * updateCluster
   * @param params The `UiAwsClusterControllerService.UpdateClusterUsingPUT1Params` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  updateClusterUsingPUT1(params: UiAwsClusterControllerService.UpdateClusterUsingPUT1Params): __Observable<AwsCluster> {
    return this.updateClusterUsingPUT1Response(params).pipe(
      __map(_r => _r.body as AwsCluster)
    );
  }
}

module UiAwsClusterControllerService {

  /**
   * Parameters for updateClusterUsingPUT1
   */
  export interface UpdateClusterUsingPUT1Params {

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

export { UiAwsClusterControllerService }
