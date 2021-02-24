/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { Stack } from '../models/stack';
import { ClusterTask } from '../models/cluster-task';
import { ClusterTaskRequest } from '../models/cluster-task-request';
import { Substack } from '../models/substack';
import { AbstractCluster } from '../models/abstract-cluster';
import { DeploymentContext } from '../models/deployment-context';
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
  static readonly createClusterTasksUsingPOST1Path = '/cc-ui/v1/stacks/clusterTask';
  static readonly getAllClusterTasksUsingGETPath = '/cc-ui/v1/stacks/clusterTask/{stackName}';
  static readonly createSubStackUsingPOSTPath = '/cc-ui/v1/stacks/substack/{substackName}';
  static readonly getStackUsingGETPath = '/cc-ui/v1/stacks/{stackName}';
  static readonly updateStackUsingPUT1Path = '/cc-ui/v1/stacks/{stackName}';
  static readonly getClustersUsingGET1Path = '/cc-ui/v1/stacks/{stackName}/clusters';
  static readonly getLocalDeploymentContextUsingGETPath = '/cc-ui/v1/stacks/{stackName}/localDeploymentContext';
  static readonly getAllSubscriptionsUsingGETPath = '/cc-ui/v1/stacks/{stackName}/notification/subscriptions';
  static readonly createSubscriptionUsingPOSTPath = '/cc-ui/v1/stacks/{stackName}/notification/subscriptions';
  static readonly reloadStackUsingGET1Path = '/cc-ui/v1/stacks/{stackName}/reload';
  static readonly getResourceTypesUsingGETPath = '/cc-ui/v1/stacks/{stackName}/suggestions/resourceType';
  static readonly getResourcesByTypesUsingGETPath = '/cc-ui/v1/stacks/{stackName}/suggestions/resourceType/{resourceType}';
  static readonly toggleReleaseUsingPOST1Path = '/cc-ui/v1/stacks/{stackName}/toggleRelease';

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
   * @param taskRequest taskRequest
   * @return OK
   */
  createClusterTasksUsingPOST1Response(taskRequest: ClusterTaskRequest): __Observable<__StrictHttpResponse<Array<ClusterTask>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = taskRequest;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/stacks/clusterTask`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ClusterTask>>;
      })
    );
  }
  /**
   * @param taskRequest taskRequest
   * @return OK
   */
  createClusterTasksUsingPOST1(taskRequest: ClusterTaskRequest): __Observable<Array<ClusterTask>> {
    return this.createClusterTasksUsingPOST1Response(taskRequest).pipe(
      __map(_r => _r.body as Array<ClusterTask>)
    );
  }

  /**
   * @param stackName stackName
   * @return OK
   */
  getAllClusterTasksUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Array<ClusterTask>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/clusterTask/${stackName}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ClusterTask>>;
      })
    );
  }
  /**
   * @param stackName stackName
   * @return OK
   */
  getAllClusterTasksUsingGET(stackName: string): __Observable<Array<ClusterTask>> {
    return this.getAllClusterTasksUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Array<ClusterTask>)
    );
  }

  /**
   * @param params The `UiStackControllerService.CreateSubStackUsingPOSTParams` containing the following parameters:
   *
   * - `substackName`: substackName
   *
   * - `subStack`: subStack
   *
   * @return OK
   */
  createSubStackUsingPOSTResponse(params: UiStackControllerService.CreateSubStackUsingPOSTParams): __Observable<__StrictHttpResponse<Substack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.subStack;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/stacks/substack/${params.substackName}`,
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
   * @param params The `UiStackControllerService.CreateSubStackUsingPOSTParams` containing the following parameters:
   *
   * - `substackName`: substackName
   *
   * - `subStack`: subStack
   *
   * @return OK
   */
  createSubStackUsingPOST(params: UiStackControllerService.CreateSubStackUsingPOSTParams): __Observable<Substack> {
    return this.createSubStackUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Substack)
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
   * @param params The `UiStackControllerService.UpdateStackUsingPUT1Params` containing the following parameters:
   *
   * - `stackName`: stackName
   *
   * - `stack`: stack
   *
   * @return OK
   */
  updateStackUsingPUT1Response(params: UiStackControllerService.UpdateStackUsingPUT1Params): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.stack;
    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc-ui/v1/stacks/${params.stackName}`,
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
   * @param params The `UiStackControllerService.UpdateStackUsingPUT1Params` containing the following parameters:
   *
   * - `stackName`: stackName
   *
   * - `stack`: stack
   *
   * @return OK
   */
  updateStackUsingPUT1(params: UiStackControllerService.UpdateStackUsingPUT1Params): __Observable<Stack> {
    return this.updateStackUsingPUT1Response(params).pipe(
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

  /**
   * @param stackName stackName
   * @return OK
   */
  getLocalDeploymentContextUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<DeploymentContext>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${stackName}/localDeploymentContext`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<DeploymentContext>;
      })
    );
  }
  /**
   * @param stackName stackName
   * @return OK
   */
  getLocalDeploymentContextUsingGET(stackName: string): __Observable<DeploymentContext> {
    return this.getLocalDeploymentContextUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as DeploymentContext)
    );
  }

  /**
   * @param stackName stackName
   * @return OK
   */
  getAllSubscriptionsUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Array<Subscription>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${stackName}/notification/subscriptions`,
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
   * @param stackName stackName
   * @return OK
   */
  getAllSubscriptionsUsingGET(stackName: string): __Observable<Array<Subscription>> {
    return this.getAllSubscriptionsUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Array<Subscription>)
    );
  }

  /**
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
      this.rootUrl + `/cc-ui/v1/stacks/${params.stackName}/notification/subscriptions`,
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
   * @param stackName stackName
   * @return OK
   */
  reloadStackUsingGET1Response(stackName: string): __Observable<__StrictHttpResponse<Stack>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${stackName}/reload`,
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
  reloadStackUsingGET1(stackName: string): __Observable<Stack> {
    return this.reloadStackUsingGET1Response(stackName).pipe(
      __map(_r => _r.body as Stack)
    );
  }

  /**
   * @param stackName stackName
   * @return OK
   */
  getResourceTypesUsingGETResponse(stackName: string): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/stacks/${stackName}/suggestions/resourceType`,
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
   * @param stackName stackName
   * @return OK
   */
  getResourceTypesUsingGET(stackName: string): __Observable<Array<string>> {
    return this.getResourceTypesUsingGETResponse(stackName).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }

  /**
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
      this.rootUrl + `/cc-ui/v1/stacks/${params.stackName}/suggestions/resourceType/${params.resourceType}`,
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
   * @param params The `UiStackControllerService.ToggleReleaseUsingPOST1Params` containing the following parameters:
   *
   * - `toggleRelease`: toggleRelease
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  toggleReleaseUsingPOST1Response(params: UiStackControllerService.ToggleReleaseUsingPOST1Params): __Observable<__StrictHttpResponse<ToggleRelease>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.toggleRelease;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/stacks/${params.stackName}/toggleRelease`,
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
   * @param params The `UiStackControllerService.ToggleReleaseUsingPOST1Params` containing the following parameters:
   *
   * - `toggleRelease`: toggleRelease
   *
   * - `stackName`: stackName
   *
   * @return OK
   */
  toggleReleaseUsingPOST1(params: UiStackControllerService.ToggleReleaseUsingPOST1Params): __Observable<ToggleRelease> {
    return this.toggleReleaseUsingPOST1Response(params).pipe(
      __map(_r => _r.body as ToggleRelease)
    );
  }
}

module UiStackControllerService {

  /**
   * Parameters for createSubStackUsingPOST
   */
  export interface CreateSubStackUsingPOSTParams {

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
   * Parameters for updateStackUsingPUT1
   */
  export interface UpdateStackUsingPUT1Params {

    /**
     * stackName
     */
    stackName: string;

    /**
     * stack
     */
    stack: Stack;
  }

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
   * Parameters for toggleReleaseUsingPOST1
   */
  export interface ToggleReleaseUsingPOST1Params {

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
