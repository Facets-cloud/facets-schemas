/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { Stack } from '../models/stack';
import { Substack } from '../models/substack';
import { AbstractCluster } from '../models/abstract-cluster';
import { ToggleRelease } from '../models/toggle-release';

/**
 * Stack Controller
 */
@Injectable({
  providedIn: 'root',
})
class StackControllerService extends __BaseService {
  static readonly getStacksUsingGETPath = '/cc/v1/stacks/';
  static readonly createStackUsingPOSTPath = '/cc/v1/stacks/';
  static readonly getSubstacksUsingGETPath = '/cc/v1/stacks/substack';
  static readonly getSubstackUsingGETPath = '/cc/v1/stacks/substack/{substackName}';
  static readonly createSubstackUsingPOSTPath = '/cc/v1/stacks/substack/{substackName}';
  static readonly getClustersUsingGETPath = '/cc/v1/stacks/{stackName}/clusters';
  static readonly reloadStackUsingGETPath = '/cc/v1/stacks/{stackName}/reload';
  static readonly toggleReleaseUsingPOSTPath = '/cc/v1/stacks/{stackName}/toggleRelease';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getStacks
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
   * getStacks
   * @return OK
   */
  getStacksUsingGET(): __Observable<Array<Stack>> {
    return this.getStacksUsingGETResponse().pipe(
      __map(_r => _r.body as Array<Stack>)
    );
  }

  /**
   * createStack
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
   * createStack
   * @param stack stack
   * @return OK
   */
  createStackUsingPOST(stack: Stack): __Observable<Stack> {
    return this.createStackUsingPOSTResponse(stack).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * getSubstacks
   * @param stackName stackName
   * @return OK
   */
  getSubstacksUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Array<Substack>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/stacks/substack`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Substack>>;
      })
    );
  }
  /**
   * getSubstacks
   * @param stackName stackName
   * @return OK
   */
  getSubstacksUsingGET(stackName: string): __Observable<Array<Substack>> {
    return this.getSubstacksUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Array<Substack>)
    );
  }

  /**
   * getSubstack
   * @param params The `StackControllerService.GetSubstackUsingGETParams` containing the following parameters:
   *
   * - `substackName`: substackName
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  getSubstackUsingGETResponse(params: StackControllerService.GetSubstackUsingGETParams): __Observable<__StrictHttpResponse<Substack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/stacks/substack/${encodeURIComponent(params.substackName)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Substack>;
      })
    );
  }
  /**
   * getSubstack
   * @param params The `StackControllerService.GetSubstackUsingGETParams` containing the following parameters:
   *
   * - `substackName`: substackName
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  getSubstackUsingGET(params: StackControllerService.GetSubstackUsingGETParams): __Observable<Substack> {
    return this.getSubstackUsingGETResponse(params).pipe(
      __map(_r => _r.body as Substack)
    );
  }

  /**
   * createSubstack
   * @param params The `StackControllerService.CreateSubstackUsingPOSTParams` containing the following parameters:
   *
   * - `substackName`: substackName
   *
   * - `subStack`: subStack
   *
   * @return OK
   */
  createSubstackUsingPOSTResponse(params: StackControllerService.CreateSubstackUsingPOSTParams): __Observable<__StrictHttpResponse<Substack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.subStack;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/stacks/substack/${encodeURIComponent(params.substackName)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Substack>;
      })
    );
  }
  /**
   * createSubstack
   * @param params The `StackControllerService.CreateSubstackUsingPOSTParams` containing the following parameters:
   *
   * - `substackName`: substackName
   *
   * - `subStack`: subStack
   *
   * @return OK
   */
  createSubstackUsingPOST(params: StackControllerService.CreateSubstackUsingPOSTParams): __Observable<Substack> {
    return this.createSubstackUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Substack)
    );
  }

  /**
   * getClusters
   * @param stackName stackName
   * @return OK
   */
  getClustersUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Array<AbstractCluster>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/stacks/${encodeURIComponent(stackName)}/clusters`,
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
   * getClusters
   * @param stackName stackName
   * @return OK
   */
  getClustersUsingGET(stackName: string): __Observable<Array<AbstractCluster>> {
    return this.getClustersUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Array<AbstractCluster>)
    );
  }

  /**
   * reloadStack
   * @param stackName stackName
   * @return OK
   */
  reloadStackUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/stacks/${encodeURIComponent(stackName)}/reload`,
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
   * reloadStack
   * @param stackName stackName
   * @return OK
   */
  reloadStackUsingGET(stackName: string): __Observable<Stack> {
    return this.reloadStackUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * toggleRelease
   * @param params The `StackControllerService.ToggleReleaseUsingPOSTParams` containing the following parameters:
   *
   * - `toggleRelease`: toggleRelease
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  toggleReleaseUsingPOSTResponse(params: StackControllerService.ToggleReleaseUsingPOSTParams): __Observable<__StrictHttpResponse<ToggleRelease>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.toggleRelease;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/stacks/${encodeURIComponent(params.stackName)}/toggleRelease`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ToggleRelease>;
      })
    );
  }
  /**
   * toggleRelease
   * @param params The `StackControllerService.ToggleReleaseUsingPOSTParams` containing the following parameters:
   *
   * - `toggleRelease`: toggleRelease
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  toggleReleaseUsingPOST(params: StackControllerService.ToggleReleaseUsingPOSTParams): __Observable<ToggleRelease> {
    return this.toggleReleaseUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as ToggleRelease)
    );
  }
}

module StackControllerService {

  /**
   * Parameters for getSubstackUsingGET
   */
  export interface GetSubstackUsingGETParams {

    /**
     * substackName
     */
    substackName: string;

    /**
     * stackName
     */
    stackName: string;
  }

  /**
   * Parameters for createSubstackUsingPOST
   */
  export interface CreateSubstackUsingPOSTParams {

    /**
     * substackName
     */
    substackName: string;

    /**
     * subStack
     */
    subStack: Substack;
  }

  /**
   * Parameters for toggleReleaseUsingPOST
   */
  export interface ToggleReleaseUsingPOSTParams {

    /**
     * toggleRelease
     */
    toggleRelease: ToggleRelease;

    /**
     * stackName
     */
    stackName: string;
  }
}

export { StackControllerService }
