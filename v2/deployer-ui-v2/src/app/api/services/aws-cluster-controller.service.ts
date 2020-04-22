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
import { K8sCredentials } from '../models/k8s-credentials';

/**
 * Aws Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class AwsClusterControllerService extends __BaseService {
  static readonly createClusterUsingPOSTPath = '/cc/v1/aws/clusters';
  static readonly getClusterUsingGETPath = '/cc/v1/aws/clusters/{clusterId}';
  static readonly addClusterK8sCredentialsUsingPOSTPath = '/cc/v1/aws/clusters/{clusterId}/credentials';

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
   * @param params The `AwsClusterControllerService.AddClusterK8sCredentialsUsingPOSTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  addClusterK8sCredentialsUsingPOSTResponse(params: AwsClusterControllerService.AddClusterK8sCredentialsUsingPOSTParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.request;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/aws/clusters/${params.clusterId}/credentials`,
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
   * @param params The `AwsClusterControllerService.AddClusterK8sCredentialsUsingPOSTParams` containing the following parameters:
   *
   * - `request`: request
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  addClusterK8sCredentialsUsingPOST(params: AwsClusterControllerService.AddClusterK8sCredentialsUsingPOSTParams): __Observable<boolean> {
    return this.addClusterK8sCredentialsUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }
}

module AwsClusterControllerService {

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
}

export { AwsClusterControllerService }
