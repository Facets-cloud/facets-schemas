/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { CodeBuildStatusCallback } from '../models/code-build-status-callback';
import { DRResult } from '../models/drresult';

/**
 * Capillary Cloud Callback Controller
 */
@Injectable({
  providedIn: 'root',
})
class CapillaryCloudCallbackControllerService extends __BaseService {
  static readonly codeBuildCallbackUsingPOSTPath = '/cc/v1/callbacks/codebuild';
  static readonly drResultCallbackUsingPOSTPath = '/cc/v1/callbacks/{cluster}/dr/{moduleType}/{instanceName}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * codeBuildCallback
   * @param callback callback
   * @return OK
   */
  codeBuildCallbackUsingPOSTResponse(callback: CodeBuildStatusCallback): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = callback;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/callbacks/codebuild`,
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
   * codeBuildCallback
   * @param callback callback
   * @return OK
   */
  codeBuildCallbackUsingPOST(callback: CodeBuildStatusCallback): __Observable<boolean> {
    return this.codeBuildCallbackUsingPOSTResponse(callback).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * drResultCallback
   * @param params The `CapillaryCloudCallbackControllerService.DrResultCallbackUsingPOSTParams` containing the following parameters:
   *
   * - `moduleType`: moduleType
   *
   * - `instanceName`: instanceName
   *
   * - `cluster`: cluster
   *
   * - `callback`: callback
   *
   * @return OK
   */
  drResultCallbackUsingPOSTResponse(params: CapillaryCloudCallbackControllerService.DrResultCallbackUsingPOSTParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    __body = params.callback;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/callbacks/${encodeURIComponent(params.cluster)}/dr/${encodeURIComponent(params.moduleType)}/${encodeURIComponent(params.instanceName)}`,
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
   * drResultCallback
   * @param params The `CapillaryCloudCallbackControllerService.DrResultCallbackUsingPOSTParams` containing the following parameters:
   *
   * - `moduleType`: moduleType
   *
   * - `instanceName`: instanceName
   *
   * - `cluster`: cluster
   *
   * - `callback`: callback
   *
   * @return OK
   */
  drResultCallbackUsingPOST(params: CapillaryCloudCallbackControllerService.DrResultCallbackUsingPOSTParams): __Observable<boolean> {
    return this.drResultCallbackUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }
}

module CapillaryCloudCallbackControllerService {

  /**
   * Parameters for drResultCallbackUsingPOST
   */
  export interface DrResultCallbackUsingPOSTParams {

    /**
     * moduleType
     */
    moduleType: string;

    /**
     * instanceName
     */
    instanceName: string;

    /**
     * cluster
     */
    cluster: string;

    /**
     * callback
     */
    callback: DRResult;
  }
}

export { CapillaryCloudCallbackControllerService }
