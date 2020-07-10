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
import { QASuiteResult } from '../models/qasuite-result';

/**
 * Deployment Controller
 */
@Injectable({
  providedIn: 'root',
})
class DeploymentControllerService extends __BaseService {
  static readonly getDeploymentsUsingGETPath = '/cc/v1/clusters/{clusterId}/deployments';
  static readonly createDeploymentUsingPOSTPath = '/cc/v1/clusters/{clusterId}/deployments';
  static readonly triggerAutomationSuiteUsingPOSTPath = '/cc/v1/clusters/{clusterId}/deployments/qa/triggerSuite';
  static readonly validateSanityResultUsingPOSTPath = '/cc/v1/clusters/{clusterId}/deployments/qa/validateSanityResult';
  static readonly abortAutomationSuiteUsingDELETEPath = '/cc/v1/clusters/{clusterId}/deployments/qa/{executionId}/abortSuite';
  static readonly getAutomationSuiteStatusUsingGETPath = '/cc/v1/clusters/{clusterId}/deployments/qa/{executionId}/status';
  static readonly getLogsUsingGETPath = '/cc/v1/clusters/{clusterId}/deployments/{id}';

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
  getDeploymentsUsingGETResponse(clusterId: string): __Observable<__StrictHttpResponse<Array<DeploymentLog>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(clusterId)}/deployments`,
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
  getDeploymentsUsingGET(clusterId: string): __Observable<Array<DeploymentLog>> {
    return this.getDeploymentsUsingGETResponse(clusterId).pipe(
      __map(_r => _r.body as Array<DeploymentLog>)
    );
  }

  /**
   * createDeployment
   * @param params The `DeploymentControllerService.CreateDeploymentUsingPOSTParams` containing the following parameters:
   *
   * - `deploymentRequest`: deploymentRequest
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  createDeploymentUsingPOSTResponse(params: DeploymentControllerService.CreateDeploymentUsingPOSTParams): __Observable<__StrictHttpResponse<DeploymentLog>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.deploymentRequest;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments`,
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
   * @param params The `DeploymentControllerService.CreateDeploymentUsingPOSTParams` containing the following parameters:
   *
   * - `deploymentRequest`: deploymentRequest
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  createDeploymentUsingPOST(params: DeploymentControllerService.CreateDeploymentUsingPOSTParams): __Observable<DeploymentLog> {
    return this.createDeploymentUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as DeploymentLog)
    );
  }

  /**
   * triggerAutomationSuite
   * @param params The `DeploymentControllerService.TriggerAutomationSuiteUsingPOSTParams` containing the following parameters:
   *
   * - `clusterId`: clusterId
   *
   * - `automationSuite`: automationSuite
   *
   * @return OK
   */
  triggerAutomationSuiteUsingPOSTResponse(params: DeploymentControllerService.TriggerAutomationSuiteUsingPOSTParams): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.automationSuite;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments/qa/triggerSuite`,
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
   * @param params The `DeploymentControllerService.TriggerAutomationSuiteUsingPOSTParams` containing the following parameters:
   *
   * - `clusterId`: clusterId
   *
   * - `automationSuite`: automationSuite
   *
   * @return OK
   */
  triggerAutomationSuiteUsingPOST(params: DeploymentControllerService.TriggerAutomationSuiteUsingPOSTParams): __Observable<string> {
    return this.triggerAutomationSuiteUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * validateSanityResult
   * @param params The `DeploymentControllerService.ValidateSanityResultUsingPOSTParams` containing the following parameters:
   *
   * - `qaSuiteResult`: qaSuiteResult
   *
   * - `clusterId`: clusterId
   */
  validateSanityResultUsingPOSTResponse(params: DeploymentControllerService.ValidateSanityResultUsingPOSTParams): __Observable<__StrictHttpResponse<null>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.qaSuiteResult;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments/qa/validateSanityResult`,
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
   * validateSanityResult
   * @param params The `DeploymentControllerService.ValidateSanityResultUsingPOSTParams` containing the following parameters:
   *
   * - `qaSuiteResult`: qaSuiteResult
   *
   * - `clusterId`: clusterId
   */
  validateSanityResultUsingPOST(params: DeploymentControllerService.ValidateSanityResultUsingPOSTParams): __Observable<null> {
    return this.validateSanityResultUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as null)
    );
  }

  /**
   * abortAutomationSuite
   * @param params The `DeploymentControllerService.AbortAutomationSuiteUsingDELETEParams` containing the following parameters:
   *
   * - `executionId`: executionId
   *
   * - `clusterId`: clusterId
   */
  abortAutomationSuiteUsingDELETEResponse(params: DeploymentControllerService.AbortAutomationSuiteUsingDELETEParams): __Observable<__StrictHttpResponse<null>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments/qa/${encodeURIComponent(params.executionId)}/abortSuite`,
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
   * @param params The `DeploymentControllerService.AbortAutomationSuiteUsingDELETEParams` containing the following parameters:
   *
   * - `executionId`: executionId
   *
   * - `clusterId`: clusterId
   */
  abortAutomationSuiteUsingDELETE(params: DeploymentControllerService.AbortAutomationSuiteUsingDELETEParams): __Observable<null> {
    return this.abortAutomationSuiteUsingDELETEResponse(params).pipe(
      __map(_r => _r.body as null)
    );
  }

  /**
   * getAutomationSuiteStatus
   * @param params The `DeploymentControllerService.GetAutomationSuiteStatusUsingGETParams` containing the following parameters:
   *
   * - `executionId`: executionId
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getAutomationSuiteStatusUsingGETResponse(params: DeploymentControllerService.GetAutomationSuiteStatusUsingGETParams): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments/qa/${encodeURIComponent(params.executionId)}/status`,
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
   * getAutomationSuiteStatus
   * @param params The `DeploymentControllerService.GetAutomationSuiteStatusUsingGETParams` containing the following parameters:
   *
   * - `executionId`: executionId
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getAutomationSuiteStatusUsingGET(params: DeploymentControllerService.GetAutomationSuiteStatusUsingGETParams): __Observable<string> {
    return this.getAutomationSuiteStatusUsingGETResponse(params).pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * getLogs
   * @param params The `DeploymentControllerService.GetLogsUsingGETParams` containing the following parameters:
   *
   * - `id`: id
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getLogsUsingGETResponse(params: DeploymentControllerService.GetLogsUsingGETParams): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/clusters/${encodeURIComponent(params.clusterId)}/deployments/${encodeURIComponent(params.id)}`,
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
   * @param params The `DeploymentControllerService.GetLogsUsingGETParams` containing the following parameters:
   *
   * - `id`: id
   *
   * - `clusterId`: clusterId
   *
   * @return OK
   */
  getLogsUsingGET(params: DeploymentControllerService.GetLogsUsingGETParams): __Observable<Array<string>> {
    return this.getLogsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }
}

module DeploymentControllerService {

  /**
   * Parameters for createDeploymentUsingPOST
   */
  export interface CreateDeploymentUsingPOSTParams {

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
   * Parameters for triggerAutomationSuiteUsingPOST
   */
  export interface TriggerAutomationSuiteUsingPOSTParams {

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
   * Parameters for validateSanityResultUsingPOST
   */
  export interface ValidateSanityResultUsingPOSTParams {

    /**
     * qaSuiteResult
     */
    qaSuiteResult: QASuiteResult;

    /**
     * clusterId
     */
    clusterId: string;
  }

  /**
   * Parameters for abortAutomationSuiteUsingDELETE
   */
  export interface AbortAutomationSuiteUsingDELETEParams {

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
   * Parameters for getAutomationSuiteStatusUsingGET
   */
  export interface GetAutomationSuiteStatusUsingGETParams {

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
   * Parameters for getLogsUsingGET
   */
  export interface GetLogsUsingGETParams {

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

export { DeploymentControllerService }
