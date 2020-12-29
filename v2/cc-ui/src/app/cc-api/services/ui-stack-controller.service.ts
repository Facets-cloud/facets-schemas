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
import { Subscription } from '../models/subscription';
import { ToggleRelease } from '../models/toggle-release';

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
  static readonly getClustersUsingGET1Path = '/cc-ui/v1/stacks/{stackName}/clusters';
  static readonly getAllSubscriptionsUsingGETPath = '/cc-ui/v1/stacks/{stackName}/notification/subscriptions';
  static readonly createSubscriptionUsingPOSTPath = '/cc-ui/v1/stacks/{stackName}/notification/subscriptions';
  static readonly reloadStackUsingGET1Path = '/cc-ui/v1/stacks/{stackName}/reload';
  static readonly getResourceTypesUsingGETPath = '/cc-ui/v1/stacks/{stackName}/suggestions/resourceType';
  static readonly getResourcesByTypesUsingGETPath = '/cc-ui/v1/stacks/{stackName}/suggestions/resourceType/{resourceType}';
  static readonly toggleReleaseUsingPOSTPath = '/cc-ui/v1/stacks/{stackName}/toggleRelease';

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
   * getStacks
   * @return OK
   */
  getStacksUsingGET1(): __Observable<Array<Stack>> {
    return this.getStacksUsingGET1Response().pipe(
      __map(_r => _r.body as Array<Stack>)
    );
  }

  /**
   * createStack
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
   * createStack
   * @param stack stack
   * @return OK
   */
  createStackUsingPOST1(stack: Stack): __Observable<Stack> {
    return this.createStackUsingPOST1Response(stack).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * getStack
   * @param stackName stackName
   * @return OK
   */
  getStackUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${encodeURIComponent(stackName)}`,
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
   * getStack
   * @param stackName stackName
   * @return OK
   */
  getStackUsingGET(stackName: string): __Observable<Stack> {
    return this.getStackUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * getClusters
   * @param stackName stackName
   * @return OK
   */
  getClustersUsingGET1Response(stackName: string): __Observable<__StrictHttpResponse<Array<AbstractCluster>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${encodeURIComponent(stackName)}/clusters`,
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
  getClustersUsingGET1(stackName: string): __Observable<Array<AbstractCluster>> {
    return this.getClustersUsingGET1Response(stackName).pipe(
      __map(_r => _r.body as Array<AbstractCluster>)
    );
  }

  /**
   * getAllSubscriptions
   * @param stackName stackName
   * @return OK
   */
  getAllSubscriptionsUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Array<Subscription>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${encodeURIComponent(stackName)}/notification/subscriptions`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Subscription>>;
      })
    );
  }
  /**
   * getAllSubscriptions
   * @param stackName stackName
   * @return OK
   */
  getAllSubscriptionsUsingGET(stackName: string): __Observable<Array<Subscription>> {
    return this.getAllSubscriptionsUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Array<Subscription>)
    );
  }

  /**
   * createSubscription
   * @param params The `UiStackControllerService.CreateSubscriptionUsingPOSTParams` containing the following parameters:
   *
   * - `subscription`: subscription
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  createSubscriptionUsingPOSTResponse(params: UiStackControllerService.CreateSubscriptionUsingPOSTParams): __Observable<__StrictHttpResponse<Subscription>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.subscription;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/stacks/${encodeURIComponent(params.stackName)}/notification/subscriptions`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Subscription>;
      })
    );
  }
  /**
   * createSubscription
   * @param params The `UiStackControllerService.CreateSubscriptionUsingPOSTParams` containing the following parameters:
   *
   * - `subscription`: subscription
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  createSubscriptionUsingPOST(params: UiStackControllerService.CreateSubscriptionUsingPOSTParams): __Observable<Subscription> {
    return this.createSubscriptionUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Subscription)
    );
  }

  /**
   * reloadStack
   * @param stackName stackName
   * @return OK
   */
  reloadStackUsingGET1Response(stackName: string): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${encodeURIComponent(stackName)}/reload`,
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
  reloadStackUsingGET1(stackName: string): __Observable<Stack> {
    return this.reloadStackUsingGET1Response(stackName).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * getResourceTypes
   * @param stackName stackName
   * @return OK
   */
  getResourceTypesUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${encodeURIComponent(stackName)}/suggestions/resourceType`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<string>>;
      })
    );
  }
  /**
   * getResourceTypes
   * @param stackName stackName
   * @return OK
   */
  getResourceTypesUsingGET(stackName: string): __Observable<Array<string>> {
    return this.getResourceTypesUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }

  /**
   * getResourcesByTypes
   * @param params The `UiStackControllerService.GetResourcesByTypesUsingGETParams` containing the following parameters:
   *
   * - `stackName`: stackName
   *
   * - `resourceType`: resourceType
   *
   * @return OK
   */
  getResourcesByTypesUsingGETResponse(params: UiStackControllerService.GetResourcesByTypesUsingGETParams): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${encodeURIComponent(params.stackName)}/suggestions/resourceType/${encodeURIComponent(params.resourceType)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<string>>;
      })
    );
  }
  /**
   * getResourcesByTypes
   * @param params The `UiStackControllerService.GetResourcesByTypesUsingGETParams` containing the following parameters:
   *
   * - `stackName`: stackName
   *
   * - `resourceType`: resourceType
   *
   * @return OK
   */
  getResourcesByTypesUsingGET(params: UiStackControllerService.GetResourcesByTypesUsingGETParams): __Observable<Array<string>> {
    return this.getResourcesByTypesUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }

  /**
   * toggleRelease
   * @param params The `UiStackControllerService.ToggleReleaseUsingPOSTParams` containing the following parameters:
   *
   * - `toggleRelease`: toggleRelease
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  toggleReleaseUsingPOSTResponse(params: UiStackControllerService.ToggleReleaseUsingPOSTParams): __Observable<__StrictHttpResponse<ToggleRelease>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.toggleRelease;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/stacks/${encodeURIComponent(params.stackName)}/toggleRelease`,
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
   * @param params The `UiStackControllerService.ToggleReleaseUsingPOSTParams` containing the following parameters:
   *
   * - `toggleRelease`: toggleRelease
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  toggleReleaseUsingPOST(params: UiStackControllerService.ToggleReleaseUsingPOSTParams): __Observable<ToggleRelease> {
    return this.toggleReleaseUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as ToggleRelease)
    );
  }
}

module UiStackControllerService {

  /**
   * Parameters for createSubscriptionUsingPOST
   */
  export interface CreateSubscriptionUsingPOSTParams {

    /**
     * subscription
     */
    subscription: Subscription;

    /**
     * stackName
     */
    stackName: string;
  }

  /**
   * Parameters for getResourcesByTypesUsingGET
   */
  export interface GetResourcesByTypesUsingGETParams {

    /**
     * stackName
     */
    stackName: string;

    /**
     * resourceType
     */
    resourceType: string;
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

export { UiStackControllerService }
