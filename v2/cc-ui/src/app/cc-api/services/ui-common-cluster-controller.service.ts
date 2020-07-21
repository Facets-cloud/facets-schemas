/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { OverrideObject } from '../models/override-object';
import { OverrideRequest } from '../models/override-request';

/**
 * Ui Common Cluster Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiCommonClusterControllerService extends __BaseService {
  static readonly getOverridesUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/overrides';
  static readonly overrideSizingUsingPOST1Path = '/cc-ui/v1/clusters/{clusterId}/overrides';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
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
}

module UiCommonClusterControllerService {

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
}

export { UiCommonClusterControllerService }
