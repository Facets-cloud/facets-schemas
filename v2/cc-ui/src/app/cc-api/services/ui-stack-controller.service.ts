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
 * Ui Stack Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiStackControllerService extends __BaseService {
  static readonly getStacksUsingGET1Path = '/cc-ui/v1/stacks/';
  static readonly createStackUsingPOST1Path = '/cc-ui/v1/stacks/';
  static readonly getStackUsingGETPath = '/cc-ui/v1/stacks/{stackName}';
  static readonly getClustersUsingGET1Path = '/cc-ui/v1/stacks/{stackName}/tableData';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @return OK
   */
  getStacksUsingGET1Response(): __Observable<__StrictHttpResponse<Array<Stack>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/`,
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
  getStacksUsingGET1(): __Observable<Array<Stack>> {
    return this.getStacksUsingGET1Response().pipe(
      __map(_r => _r.body as Array<Stack>)
    );
  }

  /**
   * @param stack stack
   * @return OK
   */
  createStackUsingPOST1Response(stack: Stack): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = stack;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/stacks/`,
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
  createStackUsingPOST1(stack: Stack): __Observable<Stack> {
    return this.createStackUsingPOST1Response(stack).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * @param stackName stackName
   * @return OK
   */
  getStackUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${stackName}`,
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
  getStackUsingGET(stackName: string): __Observable<Stack> {
    return this.getStackUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * @param stackName stackName
   * @return OK
   */
  getClustersUsingGET1Response(stackName: string): __Observable<__StrictHttpResponse<Array<AbstractCluster>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${stackName}/clusters`,
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
  getClustersUsingGET1(stackName: string): __Observable<Array<AbstractCluster>> {
    return this.getClustersUsingGET1Response(stackName).pipe(
      __map(_r => _r.body as Array<AbstractCluster>)
    );
  }
}

module UiStackControllerService {
}

export { UiStackControllerService }
