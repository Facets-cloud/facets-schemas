/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { Stack } from '../models/stack';
import { Resource } from '../models/resource';

/**
 * Stack Controller
 */
@Injectable({
  providedIn: 'root',
})
class StackControllerService extends __BaseService {
  static readonly createStackUsingPOSTPath = '/cc/v1/stacks/';
  static readonly getClusterResourcesUsingGETPath = '/cc/v1/stacks/{stackName}/resources';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @param stack stack
   * @return OK
   */
  createStackUsingPOSTResponse(stack: Stack): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = stack;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/stacks/`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Stack>;
      })
    );
  }
  /**
   * @param stack stack
   * @return OK
   */
  createStackUsingPOST(stack: Stack): __Observable<Stack> {
    return this.createStackUsingPOSTResponse(stack).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * @param params The `StackControllerService.GetClusterResourcesUsingGETParams` containing the following parameters:
   *
   * - `stackName`: stackName
   *
   * - `type`: type
   *
   * @return OK
   */
  getClusterResourcesUsingGETResponse(params: StackControllerService.GetClusterResourcesUsingGETParams): __Observable<__StrictHttpResponse<Array<Resource>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    if (params.type != null) __params = __params.set('type', params.type.toString());
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/stacks/${params.stackName}/resources`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Resource>>;
      })
    );
  }
  /**
   * @param params The `StackControllerService.GetClusterResourcesUsingGETParams` containing the following parameters:
   *
   * - `stackName`: stackName
   *
   * - `type`: type
   *
   * @return OK
   */
  getClusterResourcesUsingGET(params: StackControllerService.GetClusterResourcesUsingGETParams): __Observable<Array<Resource>> {
    return this.getClusterResourcesUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<Resource>)
    );
  }
}

module StackControllerService {

  /**
   * Parameters for getClusterResourcesUsingGET
   */
  export interface GetClusterResourcesUsingGETParams {

    /**
     * stackName
     */
    stackName: string;

    /**
     * type
     */
    type?: string;
  }
}

export { StackControllerService }
