/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { DeploymentLog } from '../models/deployment-log';
import { DeploymentRequest } from '../models/deployment-request';
import { QASuite } from '../models/qasuite';

/**
 * Ui Deployment Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiDeploymentControllerService extends __BaseService {
  static readonly getDeploymentsUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/deployments';
  static readonly createDeploymentUsingPOST1Path = '/cc-ui/v1/clusters/{clusterId}/deployments';
  static readonly triggerAutomationSuiteUsingPOST1Path = '/cc-ui/v1/clusters/{clusterId}/deployments/qa/triggerSuite';
  static readonly abortAutomationSuiteUsingDELETE1Path = '/cc-ui/v1/clusters/{clusterId}/deployments/qa/{executionId}/abortSuite';
  static readonly getLogsUsingGET1Path = '/cc-ui/v1/clusters/{clusterId}/deployments/{id}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getDeployments
   * @param clusterId clusterId
   * @return OK
   */
  getDeploymentsUsingGET1Response(clusterId: string): __Observable<__StrictHttpResponse<Array<DeploymentLog>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(clusterId)}/deployments`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<DeploymentLog>>;
      })
    );
  }
  /**
   * getDeployments
   * @param clusterId clusterId
   * @return OK
   */
  getDeploymentsUsingGET1(clusterId: string): __Observable<Array<DeploymentLog>> {
    return this.getDeploymentsUsingGET1Response(clusterId).pipe(
      __map(_r => _r.body as Array<DeploymentLog>)
    );
  }

  /**
   * createDeployment
   * @param params The `UiDeploymentControllerService.CreateDeploymentUsingPOST1Params` containing the following parameters:
   *
   * - `deploymentRequest`: deploymentRequest
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  createDeploymentUsingPOST1Response(params: UiDeploymentControllerService.CreateDeploymentUsingPOST1Params): __Observable<__StrictHttpResponse<DeploymentLog>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.deploymentRequest;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<DeploymentLog>;
      })
    );
  }
  /**
   * createDeployment
   * @param params The `UiDeploymentControllerService.CreateDeploymentUsingPOST1Params` containing the following parameters:
   *
   * - `deploymentRequest`: deploymentRequest
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  createDeploymentUsingPOST1(params: UiDeploymentControllerService.CreateDeploymentUsingPOST1Params): __Observable<DeploymentLog> {
    return this.createDeploymentUsingPOST1Response(params).pipe(
      __map(_r => _r.body as DeploymentLog)
    );
  }

  /**
   * triggerAutomationSuite
   * @param params The `UiDeploymentControllerService.TriggerAutomationSuiteUsingPOST1Params` containing the following parameters:
   *
   * - `clusterId`: clusterId
   *
   * - `automationSuite`: automationSuite
   *
   * @return OK
   */
  triggerAutomationSuiteUsingPOST1Response(params: UiDeploymentControllerService.TriggerAutomationSuiteUsingPOST1Params): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.automationSuite;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments/qa/triggerSuite`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'text'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<string>;
      })
    );
  }
  /**
   * triggerAutomationSuite
   * @param params The `UiDeploymentControllerService.TriggerAutomationSuiteUsingPOST1Params` containing the following parameters:
   *
   * - `clusterId`: clusterId
   *
   * - `automationSuite`: automationSuite
   *
   * @return OK
   */
  triggerAutomationSuiteUsingPOST1(params: UiDeploymentControllerService.TriggerAutomationSuiteUsingPOST1Params): __Observable<string> {
    return this.triggerAutomationSuiteUsingPOST1Response(params).pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * abortAutomationSuite
   * @param params The `UiDeploymentControllerService.AbortAutomationSuiteUsingDELETE1Params` containing the following parameters:
   *
   * - `executionId`: executionId
   *
   * - `clusterId`: clusterId
   */
  abortAutomationSuiteUsingDELETE1Response(params: UiDeploymentControllerService.AbortAutomationSuiteUsingDELETE1Params): __Observable<__StrictHttpResponse<null>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments/qa/${encodeURIComponent(params.executionId)}/abortSuite`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<null>;
      })
    );
  }
  /**
   * abortAutomationSuite
   * @param params The `UiDeploymentControllerService.AbortAutomationSuiteUsingDELETE1Params` containing the following parameters:
   *
   * - `executionId`: executionId
   *
   * - `clusterId`: clusterId
   */
  abortAutomationSuiteUsingDELETE1(params: UiDeploymentControllerService.AbortAutomationSuiteUsingDELETE1Params): __Observable<null> {
    return this.abortAutomationSuiteUsingDELETE1Response(params).pipe(
      __map(_r => _r.body as null)
    );
  }

  /**
   * getLogs
   * @param params The `UiDeploymentControllerService.GetLogsUsingGET1Params` containing the following parameters:
   *
   * - `id`: id
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getLogsUsingGET1Response(params: UiDeploymentControllerService.GetLogsUsingGET1Params): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments/${encodeURIComponent(params.id)}`,
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
   * getLogs
   * @param params The `UiDeploymentControllerService.GetLogsUsingGET1Params` containing the following parameters:
   *
   * - `id`: id
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getLogsUsingGET1(params: UiDeploymentControllerService.GetLogsUsingGET1Params): __Observable<Array<string>> {
    return this.getLogsUsingGET1Response(params).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }
}

module UiDeploymentControllerService {

  /**
   * Parameters for createDeploymentUsingPOST1
   */
  export interface CreateDeploymentUsingPOST1Params {

    /**
     * deploymentRequest
     */
    deploymentRequest: DeploymentRequest;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for triggerAutomationSuiteUsingPOST1
   */
  export interface TriggerAutomationSuiteUsingPOST1Params {

    /**
     * clusterId
     */
    clusterId: string;

    /**
     * automationSuite
     */
    automationSuite: QASuite;
  }

  /**
   * Parameters for abortAutomationSuiteUsingDELETE1
   */
  export interface AbortAutomationSuiteUsingDELETE1Params {

    /**
     * executionId
     */
    executionId: string;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for getLogsUsingGET1
   */
  export interface GetLogsUsingGET1Params {

    /**
     * id
     */
    id: string;

    /**
     * clusterId
     */
    clusterId: string;
  }
}

export { UiDeploymentControllerService }
