/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { Stack } from '../models/stack';
import { AbstractCluster } from '../models/abstract-cluster';

/**
 * Stack Controller
 */
@Injectable({
  providedIn: 'root',
})
class StackControllerService extends __BaseService {
  static readonly getStacksUsingGETPath = '/cc/v1/stacks/';
  static readonly createStackUsingPOSTPath = '/cc/v1/stacks/';
  static readonly getClustersUsingGETPath = '/cc/v1/stacks/{stackName}/clusters';
  static readonly reloadStackUsingGETPath = '/cc/v1/stacks/{stackName}/reload';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @return OK
   */
  getStacksUsingGETResponse(): __Observable<__StrictHttpResponse<Array<Stack>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
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
        return _r as __StrictHttpResponse<Array<Stack>>;
      })
    );
  }
  /**
   * @return OK
   */
  getStacksUsingGET(): __Observable<Array<Stack>> {
    return this.getStacksUsingGETResponse().pipe(
      __map(_r => _r.body as Array<Stack>)
    );
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
   * @param stackName stackName
   * @return OK
   */
  getClustersUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Array<AbstractCluster>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/stacks/${stackName}/clusters`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<AbstractCluster>>;
      })
    );
  }
  /**
   * @param stackName stackName
   * @return OK
   */
  getClustersUsingGET(stackName: string): __Observable<Array<AbstractCluster>> {
    return this.getClustersUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Array<AbstractCluster>)
    );
  }

  /**
   * @param stackName stackName
   * @return OK
   */
  reloadStackUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/stacks/${stackName}/reload`,
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
   * @param stackName stackName
   * @return OK
   */
  reloadStackUsingGET(stackName: string): __Observable<Stack> {
    return this.reloadStackUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Stack)
    );
  }
}

module StackControllerService {
}

export { StackControllerService }
