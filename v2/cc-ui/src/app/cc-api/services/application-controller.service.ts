/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { ApplicationFamilyMetadata } from '../models/application-family-metadata';
import { ApplicationAction } from '../models/application-action';
import { EnvironmentMetaData } from '../models/environment-meta-data';
import { ECRRegistry } from '../models/ecrregistry';
import { EcrTokenMap } from '../models/ecr-token-map';
import { Registry } from '../models/registry';
import { SimpleOauth2User } from '../models/simple-oauth-2user';
import { GlobalStats } from '../models/global-stats';
import { User } from '../models/user';
import { PasswordChange } from '../models/password-change';
import { Application } from '../models/application';
import { Build } from '../models/build';
import { InputStreamResource } from '../models/input-stream-resource';
import { TokenPaginatedResponseLogEvent } from '../models/token-paginated-response-log-event';
import { TestBuildDetails } from '../models/test-build-details';
import { ActionExecution } from '../models/action-execution';
import { ApplicationMetrics } from '../models/application-metrics';
import { ApplicationSecretRequest } from '../models/application-secret-request';
import { BitbucketPREvent } from '../models/bitbucket-prevent';
import { GithubPREvent } from '../models/github-prevent';
import { ApplicationMetricsWrapper } from '../models/application-metrics-wrapper';
import { Environment } from '../models/environment';
import { Alerting } from '../models/alerting';
import { Deployment } from '../models/deployment';
import { DeploymentStatusDetails } from '../models/deployment-status-details';
import { Monitoring } from '../models/monitoring';
import { ApplicationPodDetails } from '../models/application-pod-details';
import { ApplicationSecret } from '../models/application-secret';

/**
 * Application Controller
 */
@Injectable({
  providedIn: 'root',
})
class ApplicationControllerService extends __BaseService {
  static readonly getApplicationFamiliesUsingGETPath = '/api/applicationFamilies';
  static readonly upsertApplicationFamilyMetadataUsingPOSTPath = '/api/applicationFamilies/{applicationFamily}/metadata';
  static readonly getApplicationTypesUsingGETPath = '/api/applicationTypes';
  static readonly createGenericActionUsingPOSTPath = '/api/buildType/{buildType}/actions';
  static readonly getCCEnvironmentMetaDataUsingGETPath = '/api/cc/{applicationFamily}/environmentMetaData';
  static readonly refreshBuildDetailsUsingPUTPath = '/api/codebuild/builds/{codeBuildId}/refresh';
  static readonly createECRRegistryUsingPOSTPath = '/api/ecrRegistry';
  static readonly getEcrTokenUsingGETPath = '/api/getEcrLoginToken';
  static readonly getAllRegistriesUsingGETPath = '/api/getRegistries';
  static readonly loginUsingGETPath = '/api/login';
  static readonly loginUsingHEADPath = '/api/login';
  static readonly loginUsingPOSTPath = '/api/login';
  static readonly loginUsingPUTPath = '/api/login';
  static readonly loginUsingDELETEPath = '/api/login';
  static readonly loginUsingOPTIONSPath = '/api/login';
  static readonly loginUsingPATCHPath = '/api/login';
  static readonly meUsingGETPath = '/api/me';
  static readonly globalStatsUsingGETPath = '/api/stats';
  static readonly getUsersUsingGETPath = '/api/users';
  static readonly createUserUsingPOSTPath = '/api/users';
  static readonly updateUserUsingPUTPath = '/api/users/{userId}';
  static readonly changePasswordUsingPUTPath = '/api/users/{userId}/changePassword';
  static readonly getApplicationsUsingGETPath = '/api/{applicationFamily}/applications';
  static readonly createApplicationUsingPOSTPath = '/api/{applicationFamily}/applications';
  static readonly updateApplicationUsingPUTPath = '/api/{applicationFamily}/applications';
  static readonly getApplicationUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}';
  static readonly deleteApplicationUsingDELETEPath = '/api/{applicationFamily}/applications/{applicationId}';
  static readonly getApplicationBranchesUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/branches';
  static readonly getBuildsUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/builds';
  static readonly buildUsingPOSTPath = '/api/{applicationFamily}/applications/{applicationId}/builds';
  static readonly getBuildUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/builds/{buildId}';
  static readonly updateBuildUsingPUTPath = '/api/{applicationFamily}/applications/{applicationId}/builds/{buildId}';
  static readonly downloadTestReportUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/builds/{buildId}/downloadArtifacts';
  static readonly getBuildLogsUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/builds/{buildId}/logs';
  static readonly getTestBuildDetailsUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/builds/{buildId}/testDetails';
  static readonly getExecutedActionsForApplicationUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/executedActions';
  static readonly getImagesUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/images';
  static readonly getApplicationMetricSummaryUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/metrics';
  static readonly getApplicationSecretRequestsUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/secretRequests';
  static readonly createAppSecretRequestUsingPOSTPath = '/api/{applicationFamily}/applications/{applicationId}/secretRequests';
  static readonly getApplicationTagsUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/tags';
  static readonly processWebhookPRBitbucketUsingPOSTPath = '/api/{applicationFamily}/applications/{applicationId}/webhooks/pr/bitbucket';
  static readonly processWebhookPRGithubUsingPOSTPath = '/api/{applicationFamily}/applications/{applicationId}/webhooks/pr/github';
  static readonly getAllApplicationMetricsUsingGETPath = '/api/{applicationFamily}/appmetrics';
  static readonly getEnvironmentMetaDataUsingGETPath = '/api/{applicationFamily}/environmentMetaData';
  static readonly getEnvironmentsUsingGETPath = '/api/{applicationFamily}/environments';
  static readonly upsertEnvironmentUsingPOSTPath = '/api/{applicationFamily}/environments';
  static readonly getEnvironmentUsingGETPath = '/api/{applicationFamily}/environments/{id}';
  static readonly getAlertingDetailsUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/alerting';
  static readonly enableAlertingUsingPOSTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/alerting';
  static readonly disableAlertingUsingDELETEPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/alerting';
  static readonly getCurrentDeploymentUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/deployment/current';
  static readonly getDeploymentStatusUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/deploymentStatus';
  static readonly deployUsingPOSTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/deployments';
  static readonly getDumpFileListUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/dumps';
  static readonly downloadDumpFileUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/dumps/download';
  static readonly haltApplicationUsingPOSTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/halt';
  static readonly getMonitoringDetailsUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/monitoring';
  static readonly enableMonitoringUsingPOSTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/monitoring';
  static readonly disableMonitoringUsingDELETEPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/monitoring';
  static readonly getApplicationPodDetailsUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/podDetails';
  static readonly getActionsForPodUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/pods/{podName}/actions';
  static readonly executeActionOnPodUsingPOSTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/pods/{podName}/actions/executeAction';
  static readonly resumeApplicationUsingPOSTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/resume';
  static readonly getApplicationSecretsUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/secretRequests';
  static readonly updateApplicationSecretsUsingPUTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/secrets';
  static readonly deleteApplicationSecretUsingDELETEPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/secrets/{secretName}';
  static readonly redeployUsingPOSTPath = '/api/{applicationFamily}/{environment}/redeployment';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getApplicationFamilies
   * @return OK
   */
  getApplicationFamiliesUsingGETResponse(): __Observable<__StrictHttpResponse<Array<'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/applicationFamilies`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>>;
      })
    );
  }
  /**
   * getApplicationFamilies
   * @return OK
   */
  getApplicationFamiliesUsingGET(): __Observable<Array<'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>> {
    return this.getApplicationFamiliesUsingGETResponse().pipe(
      __map(_r => _r.body as Array<'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>)
    );
  }

  /**
   * upsertApplicationFamilyMetadata
   * @param params The `ApplicationControllerService.UpsertApplicationFamilyMetadataUsingPOSTParams` containing the following parameters:
   *
   * - `applicationFamilyMetadata`: applicationFamilyMetadata
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  upsertApplicationFamilyMetadataUsingPOSTResponse(params: ApplicationControllerService.UpsertApplicationFamilyMetadataUsingPOSTParams): __Observable<__StrictHttpResponse<ApplicationFamilyMetadata>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.applicationFamilyMetadata;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/applicationFamilies/${encodeURIComponent(params.applicationFamily)}/metadata`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ApplicationFamilyMetadata>;
      })
    );
  }
  /**
   * upsertApplicationFamilyMetadata
   * @param params The `ApplicationControllerService.UpsertApplicationFamilyMetadataUsingPOSTParams` containing the following parameters:
   *
   * - `applicationFamilyMetadata`: applicationFamilyMetadata
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  upsertApplicationFamilyMetadataUsingPOST(params: ApplicationControllerService.UpsertApplicationFamilyMetadataUsingPOSTParams): __Observable<ApplicationFamilyMetadata> {
    return this.upsertApplicationFamilyMetadataUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as ApplicationFamilyMetadata)
    );
  }

  /**
   * getApplicationTypes
   * @return OK
   */
  getApplicationTypesUsingGETResponse(): __Observable<__StrictHttpResponse<Array<'SERVICE' | 'SCHEDULED_JOB' | 'STATEFUL_SET' | 'SERVERLESS'>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/applicationTypes`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<'SERVICE' | 'SCHEDULED_JOB' | 'STATEFUL_SET' | 'SERVERLESS'>>;
      })
    );
  }
  /**
   * getApplicationTypes
   * @return OK
   */
  getApplicationTypesUsingGET(): __Observable<Array<'SERVICE' | 'SCHEDULED_JOB' | 'STATEFUL_SET' | 'SERVERLESS'>> {
    return this.getApplicationTypesUsingGETResponse().pipe(
      __map(_r => _r.body as Array<'SERVICE' | 'SCHEDULED_JOB' | 'STATEFUL_SET' | 'SERVERLESS'>)
    );
  }

  /**
   * createGenericAction
   * @param params The `ApplicationControllerService.CreateGenericActionUsingPOSTParams` containing the following parameters:
   *
   * - `buildType`: buildType
   *
   * - `applicationAction`: applicationAction
   *
   * @return OK
   */
  createGenericActionUsingPOSTResponse(params: ApplicationControllerService.CreateGenericActionUsingPOSTParams): __Observable<__StrictHttpResponse<ApplicationAction>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.applicationAction;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/buildType/${encodeURIComponent(params.buildType)}/actions`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ApplicationAction>;
      })
    );
  }
  /**
   * createGenericAction
   * @param params The `ApplicationControllerService.CreateGenericActionUsingPOSTParams` containing the following parameters:
   *
   * - `buildType`: buildType
   *
   * - `applicationAction`: applicationAction
   *
   * @return OK
   */
  createGenericActionUsingPOST(params: ApplicationControllerService.CreateGenericActionUsingPOSTParams): __Observable<ApplicationAction> {
    return this.createGenericActionUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as ApplicationAction)
    );
  }

  /**
   * getCCEnvironmentMetaData
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getCCEnvironmentMetaDataUsingGETResponse(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<__StrictHttpResponse<Array<EnvironmentMetaData>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/cc/${encodeURIComponent(applicationFamily)}/environmentMetaData`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<EnvironmentMetaData>>;
      })
    );
  }
  /**
   * getCCEnvironmentMetaData
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getCCEnvironmentMetaDataUsingGET(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<Array<EnvironmentMetaData>> {
    return this.getCCEnvironmentMetaDataUsingGETResponse(applicationFamily).pipe(
      __map(_r => _r.body as Array<EnvironmentMetaData>)
    );
  }

  /**
   * refreshBuildDetails
   * @param codeBuildId codeBuildId
   * @return OK
   */
  refreshBuildDetailsUsingPUTResponse(codeBuildId: string): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/api/codebuild/builds/${encodeURIComponent(codeBuildId)}/refresh`,
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
   * refreshBuildDetails
   * @param codeBuildId codeBuildId
   * @return OK
   */
  refreshBuildDetailsUsingPUT(codeBuildId: string): __Observable<boolean> {
    return this.refreshBuildDetailsUsingPUTResponse(codeBuildId).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * createECRRegistry
   * @param ecrRegistry ecrRegistry
   * @return OK
   */
  createECRRegistryUsingPOSTResponse(ecrRegistry: ECRRegistry): __Observable<__StrictHttpResponse<ECRRegistry>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = ecrRegistry;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/ecrRegistry`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ECRRegistry>;
      })
    );
  }
  /**
   * createECRRegistry
   * @param ecrRegistry ecrRegistry
   * @return OK
   */
  createECRRegistryUsingPOST(ecrRegistry: ECRRegistry): __Observable<ECRRegistry> {
    return this.createECRRegistryUsingPOSTResponse(ecrRegistry).pipe(
      __map(_r => _r.body as ECRRegistry)
    );
  }

  /**
   * getEcrToken
   * @return OK
   */
  getEcrTokenUsingGETResponse(): __Observable<__StrictHttpResponse<EcrTokenMap>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/getEcrLoginToken`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<EcrTokenMap>;
      })
    );
  }
  /**
   * getEcrToken
   * @return OK
   */
  getEcrTokenUsingGET(): __Observable<EcrTokenMap> {
    return this.getEcrTokenUsingGETResponse().pipe(
      __map(_r => _r.body as EcrTokenMap)
    );
  }

  /**
   * getAllRegistries
   * @return OK
   */
  getAllRegistriesUsingGETResponse(): __Observable<__StrictHttpResponse<Array<Registry>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/getRegistries`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Registry>>;
      })
    );
  }
  /**
   * getAllRegistries
   * @return OK
   */
  getAllRegistriesUsingGET(): __Observable<Array<Registry>> {
    return this.getAllRegistriesUsingGETResponse().pipe(
      __map(_r => _r.body as Array<Registry>)
    );
  }

  /**
   * login
   * @return OK
   */
  loginUsingGETResponse(): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/login`,
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
   * login
   * @return OK
   */
  loginUsingGET(): __Observable<string> {
    return this.loginUsingGETResponse().pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * login
   * @return OK
   */
  loginUsingHEADResponse(): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'HEAD',
      this.rootUrl + `/api/login`,
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
   * login
   * @return OK
   */
  loginUsingHEAD(): __Observable<string> {
    return this.loginUsingHEADResponse().pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * login
   * @return OK
   */
  loginUsingPOSTResponse(): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/login`,
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
   * login
   * @return OK
   */
  loginUsingPOST(): __Observable<string> {
    return this.loginUsingPOSTResponse().pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * login
   * @return OK
   */
  loginUsingPUTResponse(): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/api/login`,
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
   * login
   * @return OK
   */
  loginUsingPUT(): __Observable<string> {
    return this.loginUsingPUTResponse().pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * login
   * @return OK
   */
  loginUsingDELETEResponse(): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/api/login`,
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
   * login
   * @return OK
   */
  loginUsingDELETE(): __Observable<string> {
    return this.loginUsingDELETEResponse().pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * login
   * @return OK
   */
  loginUsingOPTIONSResponse(): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'OPTIONS',
      this.rootUrl + `/api/login`,
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
   * login
   * @return OK
   */
  loginUsingOPTIONS(): __Observable<string> {
    return this.loginUsingOPTIONSResponse().pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * login
   * @return OK
   */
  loginUsingPATCHResponse(): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'PATCH',
      this.rootUrl + `/api/login`,
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
   * login
   * @return OK
   */
  loginUsingPATCH(): __Observable<string> {
    return this.loginUsingPATCHResponse().pipe(
      __map(_r => _r.body as string)
    );
  }

  /**
   * me
   * @return OK
   */
  meUsingGETResponse(): __Observable<__StrictHttpResponse<SimpleOauth2User>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/me`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<SimpleOauth2User>;
      })
    );
  }
  /**
   * me
   * @return OK
   */
  meUsingGET(): __Observable<SimpleOauth2User> {
    return this.meUsingGETResponse().pipe(
      __map(_r => _r.body as SimpleOauth2User)
    );
  }

  /**
   * globalStats
   * @return OK
   */
  globalStatsUsingGETResponse(): __Observable<__StrictHttpResponse<GlobalStats>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/stats`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<GlobalStats>;
      })
    );
  }
  /**
   * globalStats
   * @return OK
   */
  globalStatsUsingGET(): __Observable<GlobalStats> {
    return this.globalStatsUsingGETResponse().pipe(
      __map(_r => _r.body as GlobalStats)
    );
  }

  /**
   * getUsers
   * @return OK
   */
  getUsersUsingGETResponse(): __Observable<__StrictHttpResponse<Array<User>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/users`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<User>>;
      })
    );
  }
  /**
   * getUsers
   * @return OK
   */
  getUsersUsingGET(): __Observable<Array<User>> {
    return this.getUsersUsingGETResponse().pipe(
      __map(_r => _r.body as Array<User>)
    );
  }

  /**
   * createUser
   * @param user user
   * @return OK
   */
  createUserUsingPOSTResponse(user: User): __Observable<__StrictHttpResponse<User>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = user;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/users`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<User>;
      })
    );
  }
  /**
   * createUser
   * @param user user
   * @return OK
   */
  createUserUsingPOST(user: User): __Observable<User> {
    return this.createUserUsingPOSTResponse(user).pipe(
      __map(_r => _r.body as User)
    );
  }

  /**
   * updateUser
   * @param params The `ApplicationControllerService.UpdateUserUsingPUTParams` containing the following parameters:
   *
   * - `userId`: userId
   *
   * - `user`: user
   *
   * @return OK
   */
  updateUserUsingPUTResponse(params: ApplicationControllerService.UpdateUserUsingPUTParams): __Observable<__StrictHttpResponse<User>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.user;
    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/api/users/${encodeURIComponent(params.userId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<User>;
      })
    );
  }
  /**
   * updateUser
   * @param params The `ApplicationControllerService.UpdateUserUsingPUTParams` containing the following parameters:
   *
   * - `userId`: userId
   *
   * - `user`: user
   *
   * @return OK
   */
  updateUserUsingPUT(params: ApplicationControllerService.UpdateUserUsingPUTParams): __Observable<User> {
    return this.updateUserUsingPUTResponse(params).pipe(
      __map(_r => _r.body as User)
    );
  }

  /**
   * changePassword
   * @param params The `ApplicationControllerService.ChangePasswordUsingPUTParams` containing the following parameters:
   *
   * - `userId`: userId
   *
   * - `pwdChange`: pwdChange
   *
   * @return OK
   */
  changePasswordUsingPUTResponse(params: ApplicationControllerService.ChangePasswordUsingPUTParams): __Observable<__StrictHttpResponse<User>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.pwdChange;
    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/api/users/${encodeURIComponent(params.userId)}/changePassword`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<User>;
      })
    );
  }
  /**
   * changePassword
   * @param params The `ApplicationControllerService.ChangePasswordUsingPUTParams` containing the following parameters:
   *
   * - `userId`: userId
   *
   * - `pwdChange`: pwdChange
   *
   * @return OK
   */
  changePasswordUsingPUT(params: ApplicationControllerService.ChangePasswordUsingPUTParams): __Observable<User> {
    return this.changePasswordUsingPUTResponse(params).pipe(
      __map(_r => _r.body as User)
    );
  }

  /**
   * getApplications
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getApplicationsUsingGETResponse(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<__StrictHttpResponse<Array<Application>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(applicationFamily)}/applications`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Application>>;
      })
    );
  }
  /**
   * getApplications
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getApplicationsUsingGET(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<Array<Application>> {
    return this.getApplicationsUsingGETResponse(applicationFamily).pipe(
      __map(_r => _r.body as Array<Application>)
    );
  }

  /**
   * createApplication
   * @param params The `ApplicationControllerService.CreateApplicationUsingPOSTParams` containing the following parameters:
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `application`: application
   *
   * @return OK
   */
  createApplicationUsingPOSTResponse(params: ApplicationControllerService.CreateApplicationUsingPOSTParams): __Observable<__StrictHttpResponse<Application>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.application;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Application>;
      })
    );
  }
  /**
   * createApplication
   * @param params The `ApplicationControllerService.CreateApplicationUsingPOSTParams` containing the following parameters:
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `application`: application
   *
   * @return OK
   */
  createApplicationUsingPOST(params: ApplicationControllerService.CreateApplicationUsingPOSTParams): __Observable<Application> {
    return this.createApplicationUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Application)
    );
  }

  /**
   * updateApplication
   * @param params The `ApplicationControllerService.UpdateApplicationUsingPUTParams` containing the following parameters:
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `application`: application
   *
   * @return OK
   */
  updateApplicationUsingPUTResponse(params: ApplicationControllerService.UpdateApplicationUsingPUTParams): __Observable<__StrictHttpResponse<Application>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.application;
    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Application>;
      })
    );
  }
  /**
   * updateApplication
   * @param params The `ApplicationControllerService.UpdateApplicationUsingPUTParams` containing the following parameters:
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `application`: application
   *
   * @return OK
   */
  updateApplicationUsingPUT(params: ApplicationControllerService.UpdateApplicationUsingPUTParams): __Observable<Application> {
    return this.updateApplicationUsingPUTResponse(params).pipe(
      __map(_r => _r.body as Application)
    );
  }

  /**
   * getApplication
   * @param params The `ApplicationControllerService.GetApplicationUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationUsingGETResponse(params: ApplicationControllerService.GetApplicationUsingGETParams): __Observable<__StrictHttpResponse<Application>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Application>;
      })
    );
  }
  /**
   * getApplication
   * @param params The `ApplicationControllerService.GetApplicationUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationUsingGET(params: ApplicationControllerService.GetApplicationUsingGETParams): __Observable<Application> {
    return this.getApplicationUsingGETResponse(params).pipe(
      __map(_r => _r.body as Application)
    );
  }

  /**
   * deleteApplication
   * @param params The `ApplicationControllerService.DeleteApplicationUsingDELETEParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  deleteApplicationUsingDELETEResponse(params: ApplicationControllerService.DeleteApplicationUsingDELETEParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}`,
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
   * deleteApplication
   * @param params The `ApplicationControllerService.DeleteApplicationUsingDELETEParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  deleteApplicationUsingDELETE(params: ApplicationControllerService.DeleteApplicationUsingDELETEParams): __Observable<boolean> {
    return this.deleteApplicationUsingDELETEResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * getApplicationBranches
   * @param params The `ApplicationControllerService.GetApplicationBranchesUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationBranchesUsingGETResponse(params: ApplicationControllerService.GetApplicationBranchesUsingGETParams): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/branches`,
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
   * getApplicationBranches
   * @param params The `ApplicationControllerService.GetApplicationBranchesUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationBranchesUsingGET(params: ApplicationControllerService.GetApplicationBranchesUsingGETParams): __Observable<Array<string>> {
    return this.getApplicationBranchesUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }

  /**
   * getBuilds
   * @param params The `ApplicationControllerService.GetBuildsUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getBuildsUsingGETResponse(params: ApplicationControllerService.GetBuildsUsingGETParams): __Observable<__StrictHttpResponse<Array<Build>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/builds`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Build>>;
      })
    );
  }
  /**
   * getBuilds
   * @param params The `ApplicationControllerService.GetBuildsUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getBuildsUsingGET(params: ApplicationControllerService.GetBuildsUsingGETParams): __Observable<Array<Build>> {
    return this.getBuildsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<Build>)
    );
  }

  /**
   * build
   * @param params The `ApplicationControllerService.BuildUsingPOSTParams` containing the following parameters:
   *
   * - `build`: build
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  buildUsingPOSTResponse(params: ApplicationControllerService.BuildUsingPOSTParams): __Observable<__StrictHttpResponse<Build>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.build;


    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/builds`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Build>;
      })
    );
  }
  /**
   * build
   * @param params The `ApplicationControllerService.BuildUsingPOSTParams` containing the following parameters:
   *
   * - `build`: build
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  buildUsingPOST(params: ApplicationControllerService.BuildUsingPOSTParams): __Observable<Build> {
    return this.buildUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Build)
    );
  }

  /**
   * getBuild
   * @param params The `ApplicationControllerService.GetBuildUsingGETParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getBuildUsingGETResponse(params: ApplicationControllerService.GetBuildUsingGETParams): __Observable<__StrictHttpResponse<Build>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/builds/${encodeURIComponent(params.buildId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Build>;
      })
    );
  }
  /**
   * getBuild
   * @param params The `ApplicationControllerService.GetBuildUsingGETParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getBuildUsingGET(params: ApplicationControllerService.GetBuildUsingGETParams): __Observable<Build> {
    return this.getBuildUsingGETResponse(params).pipe(
      __map(_r => _r.body as Build)
    );
  }

  /**
   * updateBuild
   * @param params The `ApplicationControllerService.UpdateBuildUsingPUTParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `build`: build
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  updateBuildUsingPUTResponse(params: ApplicationControllerService.UpdateBuildUsingPUTParams): __Observable<__StrictHttpResponse<Build>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.build;


    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/builds/${encodeURIComponent(params.buildId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Build>;
      })
    );
  }
  /**
   * updateBuild
   * @param params The `ApplicationControllerService.UpdateBuildUsingPUTParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `build`: build
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  updateBuildUsingPUT(params: ApplicationControllerService.UpdateBuildUsingPUTParams): __Observable<Build> {
    return this.updateBuildUsingPUTResponse(params).pipe(
      __map(_r => _r.body as Build)
    );
  }

  /**
   * downloadTestReport
   * @param params The `ApplicationControllerService.DownloadTestReportUsingGETParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  downloadTestReportUsingGETResponse(params: ApplicationControllerService.DownloadTestReportUsingGETParams): __Observable<__StrictHttpResponse<InputStreamResource>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/builds/${encodeURIComponent(params.buildId)}/downloadArtifacts`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<InputStreamResource>;
      })
    );
  }
  /**
   * downloadTestReport
   * @param params The `ApplicationControllerService.DownloadTestReportUsingGETParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  downloadTestReportUsingGET(params: ApplicationControllerService.DownloadTestReportUsingGETParams): __Observable<InputStreamResource> {
    return this.downloadTestReportUsingGETResponse(params).pipe(
      __map(_r => _r.body as InputStreamResource)
    );
  }

  /**
   * getBuildLogs
   * @param params The `ApplicationControllerService.GetBuildLogsUsingGETParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `nextToken`: nextToken
   *
   * @return OK
   */
  getBuildLogsUsingGETResponse(params: ApplicationControllerService.GetBuildLogsUsingGETParams): __Observable<__StrictHttpResponse<TokenPaginatedResponseLogEvent>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    if (params.nextToken != null) __params = __params.set('nextToken', params.nextToken.toString());
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/builds/${encodeURIComponent(params.buildId)}/logs`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<TokenPaginatedResponseLogEvent>;
      })
    );
  }
  /**
   * getBuildLogs
   * @param params The `ApplicationControllerService.GetBuildLogsUsingGETParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `nextToken`: nextToken
   *
   * @return OK
   */
  getBuildLogsUsingGET(params: ApplicationControllerService.GetBuildLogsUsingGETParams): __Observable<TokenPaginatedResponseLogEvent> {
    return this.getBuildLogsUsingGETResponse(params).pipe(
      __map(_r => _r.body as TokenPaginatedResponseLogEvent)
    );
  }

  /**
   * getTestBuildDetails
   * @param params The `ApplicationControllerService.GetTestBuildDetailsUsingGETParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getTestBuildDetailsUsingGETResponse(params: ApplicationControllerService.GetTestBuildDetailsUsingGETParams): __Observable<__StrictHttpResponse<TestBuildDetails>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/builds/${encodeURIComponent(params.buildId)}/testDetails`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<TestBuildDetails>;
      })
    );
  }
  /**
   * getTestBuildDetails
   * @param params The `ApplicationControllerService.GetTestBuildDetailsUsingGETParams` containing the following parameters:
   *
   * - `buildId`: buildId
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getTestBuildDetailsUsingGET(params: ApplicationControllerService.GetTestBuildDetailsUsingGETParams): __Observable<TestBuildDetails> {
    return this.getTestBuildDetailsUsingGETResponse(params).pipe(
      __map(_r => _r.body as TestBuildDetails)
    );
  }

  /**
   * getExecutedActionsForApplication
   * @param params The `ApplicationControllerService.GetExecutedActionsForApplicationUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getExecutedActionsForApplicationUsingGETResponse(params: ApplicationControllerService.GetExecutedActionsForApplicationUsingGETParams): __Observable<__StrictHttpResponse<Array<ActionExecution>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/executedActions`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ActionExecution>>;
      })
    );
  }
  /**
   * getExecutedActionsForApplication
   * @param params The `ApplicationControllerService.GetExecutedActionsForApplicationUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getExecutedActionsForApplicationUsingGET(params: ApplicationControllerService.GetExecutedActionsForApplicationUsingGETParams): __Observable<Array<ActionExecution>> {
    return this.getExecutedActionsForApplicationUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<ActionExecution>)
    );
  }

  /**
   * getImages
   * @param params The `ApplicationControllerService.GetImagesUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getImagesUsingGETResponse(params: ApplicationControllerService.GetImagesUsingGETParams): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/images`,
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
   * getImages
   * @param params The `ApplicationControllerService.GetImagesUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getImagesUsingGET(params: ApplicationControllerService.GetImagesUsingGETParams): __Observable<Array<string>> {
    return this.getImagesUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }

  /**
   * getApplicationMetricSummary
   * @param params The `ApplicationControllerService.GetApplicationMetricSummaryUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationMetricSummaryUsingGETResponse(params: ApplicationControllerService.GetApplicationMetricSummaryUsingGETParams): __Observable<__StrictHttpResponse<{[key: string]: ApplicationMetrics}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/metrics`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{[key: string]: ApplicationMetrics}>;
      })
    );
  }
  /**
   * getApplicationMetricSummary
   * @param params The `ApplicationControllerService.GetApplicationMetricSummaryUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationMetricSummaryUsingGET(params: ApplicationControllerService.GetApplicationMetricSummaryUsingGETParams): __Observable<{[key: string]: ApplicationMetrics}> {
    return this.getApplicationMetricSummaryUsingGETResponse(params).pipe(
      __map(_r => _r.body as {[key: string]: ApplicationMetrics})
    );
  }

  /**
   * getApplicationSecretRequests
   * @param params The `ApplicationControllerService.GetApplicationSecretRequestsUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationSecretRequestsUsingGETResponse(params: ApplicationControllerService.GetApplicationSecretRequestsUsingGETParams): __Observable<__StrictHttpResponse<Array<ApplicationSecretRequest>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/secretRequests`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ApplicationSecretRequest>>;
      })
    );
  }
  /**
   * getApplicationSecretRequests
   * @param params The `ApplicationControllerService.GetApplicationSecretRequestsUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationSecretRequestsUsingGET(params: ApplicationControllerService.GetApplicationSecretRequestsUsingGETParams): __Observable<Array<ApplicationSecretRequest>> {
    return this.getApplicationSecretRequestsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<ApplicationSecretRequest>)
    );
  }

  /**
   * createAppSecretRequest
   * @param params The `ApplicationControllerService.CreateAppSecretRequestUsingPOSTParams` containing the following parameters:
   *
   * - `applicationSecretRequests`: applicationSecretRequests
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  createAppSecretRequestUsingPOSTResponse(params: ApplicationControllerService.CreateAppSecretRequestUsingPOSTParams): __Observable<__StrictHttpResponse<Array<ApplicationSecretRequest>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.applicationSecretRequests;


    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/secretRequests`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ApplicationSecretRequest>>;
      })
    );
  }
  /**
   * createAppSecretRequest
   * @param params The `ApplicationControllerService.CreateAppSecretRequestUsingPOSTParams` containing the following parameters:
   *
   * - `applicationSecretRequests`: applicationSecretRequests
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  createAppSecretRequestUsingPOST(params: ApplicationControllerService.CreateAppSecretRequestUsingPOSTParams): __Observable<Array<ApplicationSecretRequest>> {
    return this.createAppSecretRequestUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Array<ApplicationSecretRequest>)
    );
  }

  /**
   * getApplicationTags
   * @param params The `ApplicationControllerService.GetApplicationTagsUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationTagsUsingGETResponse(params: ApplicationControllerService.GetApplicationTagsUsingGETParams): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/tags`,
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
   * getApplicationTags
   * @param params The `ApplicationControllerService.GetApplicationTagsUsingGETParams` containing the following parameters:
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationTagsUsingGET(params: ApplicationControllerService.GetApplicationTagsUsingGETParams): __Observable<Array<string>> {
    return this.getApplicationTagsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }

  /**
   * processWebhookPRBitbucket
   * @param params The `ApplicationControllerService.ProcessWebhookPRBitbucketUsingPOSTParams` containing the following parameters:
   *
   * - `webhook`: webhook
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `X-Event-Key`: X-Event-Key
   *
   * - `Host`: Host
   *
   * @return OK
   */
  processWebhookPRBitbucketUsingPOSTResponse(params: ApplicationControllerService.ProcessWebhookPRBitbucketUsingPOSTParams): __Observable<__StrictHttpResponse<{}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.webhook;


    if (params.XEventKey != null) __headers = __headers.set('X-Event-Key', params.XEventKey.toString());
    if (params.Host != null) __headers = __headers.set('Host', params.Host.toString());
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/webhooks/pr/bitbucket`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{}>;
      })
    );
  }
  /**
   * processWebhookPRBitbucket
   * @param params The `ApplicationControllerService.ProcessWebhookPRBitbucketUsingPOSTParams` containing the following parameters:
   *
   * - `webhook`: webhook
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `X-Event-Key`: X-Event-Key
   *
   * - `Host`: Host
   *
   * @return OK
   */
  processWebhookPRBitbucketUsingPOST(params: ApplicationControllerService.ProcessWebhookPRBitbucketUsingPOSTParams): __Observable<{}> {
    return this.processWebhookPRBitbucketUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as {})
    );
  }

  /**
   * processWebhookPRGithub
   * @param params The `ApplicationControllerService.ProcessWebhookPRGithubUsingPOSTParams` containing the following parameters:
   *
   * - `webhook`: webhook
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `Host`: Host
   *
   * @return OK
   */
  processWebhookPRGithubUsingPOSTResponse(params: ApplicationControllerService.ProcessWebhookPRGithubUsingPOSTParams): __Observable<__StrictHttpResponse<{}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.webhook;


    if (params.Host != null) __headers = __headers.set('Host', params.Host.toString());
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/applications/${encodeURIComponent(params.applicationId)}/webhooks/pr/github`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{}>;
      })
    );
  }
  /**
   * processWebhookPRGithub
   * @param params The `ApplicationControllerService.ProcessWebhookPRGithubUsingPOSTParams` containing the following parameters:
   *
   * - `webhook`: webhook
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `Host`: Host
   *
   * @return OK
   */
  processWebhookPRGithubUsingPOST(params: ApplicationControllerService.ProcessWebhookPRGithubUsingPOSTParams): __Observable<{}> {
    return this.processWebhookPRGithubUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as {})
    );
  }

  /**
   * getAllApplicationMetrics
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getAllApplicationMetricsUsingGETResponse(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<__StrictHttpResponse<Array<ApplicationMetricsWrapper>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(applicationFamily)}/appmetrics`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ApplicationMetricsWrapper>>;
      })
    );
  }
  /**
   * getAllApplicationMetrics
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getAllApplicationMetricsUsingGET(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<Array<ApplicationMetricsWrapper>> {
    return this.getAllApplicationMetricsUsingGETResponse(applicationFamily).pipe(
      __map(_r => _r.body as Array<ApplicationMetricsWrapper>)
    );
  }

  /**
   * getEnvironmentMetaData
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getEnvironmentMetaDataUsingGETResponse(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<__StrictHttpResponse<Array<EnvironmentMetaData>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(applicationFamily)}/environmentMetaData`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<EnvironmentMetaData>>;
      })
    );
  }
  /**
   * getEnvironmentMetaData
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getEnvironmentMetaDataUsingGET(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<Array<EnvironmentMetaData>> {
    return this.getEnvironmentMetaDataUsingGETResponse(applicationFamily).pipe(
      __map(_r => _r.body as Array<EnvironmentMetaData>)
    );
  }

  /**
   * getEnvironments
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getEnvironmentsUsingGETResponse(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<__StrictHttpResponse<Array<Environment>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(applicationFamily)}/environments`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Environment>>;
      })
    );
  }
  /**
   * getEnvironments
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getEnvironmentsUsingGET(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<Array<Environment>> {
    return this.getEnvironmentsUsingGETResponse(applicationFamily).pipe(
      __map(_r => _r.body as Array<Environment>)
    );
  }

  /**
   * upsertEnvironment
   * @param params The `ApplicationControllerService.UpsertEnvironmentUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  upsertEnvironmentUsingPOSTResponse(params: ApplicationControllerService.UpsertEnvironmentUsingPOSTParams): __Observable<__StrictHttpResponse<Environment>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.environment;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/environments`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Environment>;
      })
    );
  }
  /**
   * upsertEnvironment
   * @param params The `ApplicationControllerService.UpsertEnvironmentUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  upsertEnvironmentUsingPOST(params: ApplicationControllerService.UpsertEnvironmentUsingPOSTParams): __Observable<Environment> {
    return this.upsertEnvironmentUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Environment)
    );
  }

  /**
   * getEnvironment
   * @param params The `ApplicationControllerService.GetEnvironmentUsingGETParams` containing the following parameters:
   *
   * - `id`: id
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getEnvironmentUsingGETResponse(params: ApplicationControllerService.GetEnvironmentUsingGETParams): __Observable<__StrictHttpResponse<Environment>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/environments/${encodeURIComponent(params.id)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Environment>;
      })
    );
  }
  /**
   * getEnvironment
   * @param params The `ApplicationControllerService.GetEnvironmentUsingGETParams` containing the following parameters:
   *
   * - `id`: id
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getEnvironmentUsingGET(params: ApplicationControllerService.GetEnvironmentUsingGETParams): __Observable<Environment> {
    return this.getEnvironmentUsingGETResponse(params).pipe(
      __map(_r => _r.body as Environment)
    );
  }

  /**
   * getAlertingDetails
   * @param params The `ApplicationControllerService.GetAlertingDetailsUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getAlertingDetailsUsingGETResponse(params: ApplicationControllerService.GetAlertingDetailsUsingGETParams): __Observable<__StrictHttpResponse<Alerting>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/alerting`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Alerting>;
      })
    );
  }
  /**
   * getAlertingDetails
   * @param params The `ApplicationControllerService.GetAlertingDetailsUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getAlertingDetailsUsingGET(params: ApplicationControllerService.GetAlertingDetailsUsingGETParams): __Observable<Alerting> {
    return this.getAlertingDetailsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Alerting)
    );
  }

  /**
   * enableAlerting
   * @param params The `ApplicationControllerService.EnableAlertingUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  enableAlertingUsingPOSTResponse(params: ApplicationControllerService.EnableAlertingUsingPOSTParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/alerting`,
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
   * enableAlerting
   * @param params The `ApplicationControllerService.EnableAlertingUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  enableAlertingUsingPOST(params: ApplicationControllerService.EnableAlertingUsingPOSTParams): __Observable<boolean> {
    return this.enableAlertingUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * disableAlerting
   * @param params The `ApplicationControllerService.DisableAlertingUsingDELETEParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  disableAlertingUsingDELETEResponse(params: ApplicationControllerService.DisableAlertingUsingDELETEParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/alerting`,
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
   * disableAlerting
   * @param params The `ApplicationControllerService.DisableAlertingUsingDELETEParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  disableAlertingUsingDELETE(params: ApplicationControllerService.DisableAlertingUsingDELETEParams): __Observable<boolean> {
    return this.disableAlertingUsingDELETEResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * getCurrentDeployment
   * @param params The `ApplicationControllerService.GetCurrentDeploymentUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getCurrentDeploymentUsingGETResponse(params: ApplicationControllerService.GetCurrentDeploymentUsingGETParams): __Observable<__StrictHttpResponse<Deployment>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/deployment/current`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Deployment>;
      })
    );
  }
  /**
   * getCurrentDeployment
   * @param params The `ApplicationControllerService.GetCurrentDeploymentUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getCurrentDeploymentUsingGET(params: ApplicationControllerService.GetCurrentDeploymentUsingGETParams): __Observable<Deployment> {
    return this.getCurrentDeploymentUsingGETResponse(params).pipe(
      __map(_r => _r.body as Deployment)
    );
  }

  /**
   * getDeploymentStatus
   * @param params The `ApplicationControllerService.GetDeploymentStatusUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getDeploymentStatusUsingGETResponse(params: ApplicationControllerService.GetDeploymentStatusUsingGETParams): __Observable<__StrictHttpResponse<DeploymentStatusDetails>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/deploymentStatus`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<DeploymentStatusDetails>;
      })
    );
  }
  /**
   * getDeploymentStatus
   * @param params The `ApplicationControllerService.GetDeploymentStatusUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getDeploymentStatusUsingGET(params: ApplicationControllerService.GetDeploymentStatusUsingGETParams): __Observable<DeploymentStatusDetails> {
    return this.getDeploymentStatusUsingGETResponse(params).pipe(
      __map(_r => _r.body as DeploymentStatusDetails)
    );
  }

  /**
   * deploy
   * @param params The `ApplicationControllerService.DeployUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `deployment`: deployment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  deployUsingPOSTResponse(params: ApplicationControllerService.DeployUsingPOSTParams): __Observable<__StrictHttpResponse<Deployment>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.deployment;


    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/deployments`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Deployment>;
      })
    );
  }
  /**
   * deploy
   * @param params The `ApplicationControllerService.DeployUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `deployment`: deployment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  deployUsingPOST(params: ApplicationControllerService.DeployUsingPOSTParams): __Observable<Deployment> {
    return this.deployUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Deployment)
    );
  }

  /**
   * getDumpFileList
   * @param params The `ApplicationControllerService.GetDumpFileListUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `date`: date
   *
   * @return OK
   */
  getDumpFileListUsingGETResponse(params: ApplicationControllerService.GetDumpFileListUsingGETParams): __Observable<__StrictHttpResponse<{[key: string]: string}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    if (params.date != null) __params = __params.set('date', params.date.toString());
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/dumps`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{[key: string]: string}>;
      })
    );
  }
  /**
   * getDumpFileList
   * @param params The `ApplicationControllerService.GetDumpFileListUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `date`: date
   *
   * @return OK
   */
  getDumpFileListUsingGET(params: ApplicationControllerService.GetDumpFileListUsingGETParams): __Observable<{[key: string]: string}> {
    return this.getDumpFileListUsingGETResponse(params).pipe(
      __map(_r => _r.body as {[key: string]: string})
    );
  }

  /**
   * downloadDumpFile
   * @param params The `ApplicationControllerService.DownloadDumpFileUsingGETParams` containing the following parameters:
   *
   * - `path`: path
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  downloadDumpFileUsingGETResponse(params: ApplicationControllerService.DownloadDumpFileUsingGETParams): __Observable<__StrictHttpResponse<InputStreamResource>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    if (params.path != null) __params = __params.set('path', params.path.toString());



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/dumps/download`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<InputStreamResource>;
      })
    );
  }
  /**
   * downloadDumpFile
   * @param params The `ApplicationControllerService.DownloadDumpFileUsingGETParams` containing the following parameters:
   *
   * - `path`: path
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  downloadDumpFileUsingGET(params: ApplicationControllerService.DownloadDumpFileUsingGETParams): __Observable<InputStreamResource> {
    return this.downloadDumpFileUsingGETResponse(params).pipe(
      __map(_r => _r.body as InputStreamResource)
    );
  }

  /**
   * haltApplication
   * @param params The `ApplicationControllerService.HaltApplicationUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  haltApplicationUsingPOSTResponse(params: ApplicationControllerService.HaltApplicationUsingPOSTParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/halt`,
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
   * haltApplication
   * @param params The `ApplicationControllerService.HaltApplicationUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  haltApplicationUsingPOST(params: ApplicationControllerService.HaltApplicationUsingPOSTParams): __Observable<boolean> {
    return this.haltApplicationUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * getMonitoringDetails
   * @param params The `ApplicationControllerService.GetMonitoringDetailsUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getMonitoringDetailsUsingGETResponse(params: ApplicationControllerService.GetMonitoringDetailsUsingGETParams): __Observable<__StrictHttpResponse<Monitoring>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/monitoring`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Monitoring>;
      })
    );
  }
  /**
   * getMonitoringDetails
   * @param params The `ApplicationControllerService.GetMonitoringDetailsUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getMonitoringDetailsUsingGET(params: ApplicationControllerService.GetMonitoringDetailsUsingGETParams): __Observable<Monitoring> {
    return this.getMonitoringDetailsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Monitoring)
    );
  }

  /**
   * enableMonitoring
   * @param params The `ApplicationControllerService.EnableMonitoringUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  enableMonitoringUsingPOSTResponse(params: ApplicationControllerService.EnableMonitoringUsingPOSTParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/monitoring`,
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
   * enableMonitoring
   * @param params The `ApplicationControllerService.EnableMonitoringUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  enableMonitoringUsingPOST(params: ApplicationControllerService.EnableMonitoringUsingPOSTParams): __Observable<boolean> {
    return this.enableMonitoringUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * disableMonitoring
   * @param params The `ApplicationControllerService.DisableMonitoringUsingDELETEParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  disableMonitoringUsingDELETEResponse(params: ApplicationControllerService.DisableMonitoringUsingDELETEParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/monitoring`,
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
   * disableMonitoring
   * @param params The `ApplicationControllerService.DisableMonitoringUsingDELETEParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  disableMonitoringUsingDELETE(params: ApplicationControllerService.DisableMonitoringUsingDELETEParams): __Observable<boolean> {
    return this.disableMonitoringUsingDELETEResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * getApplicationPodDetails
   * @param params The `ApplicationControllerService.GetApplicationPodDetailsUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationPodDetailsUsingGETResponse(params: ApplicationControllerService.GetApplicationPodDetailsUsingGETParams): __Observable<__StrictHttpResponse<Array<ApplicationPodDetails>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/podDetails`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ApplicationPodDetails>>;
      })
    );
  }
  /**
   * getApplicationPodDetails
   * @param params The `ApplicationControllerService.GetApplicationPodDetailsUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationPodDetailsUsingGET(params: ApplicationControllerService.GetApplicationPodDetailsUsingGETParams): __Observable<Array<ApplicationPodDetails>> {
    return this.getApplicationPodDetailsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<ApplicationPodDetails>)
    );
  }

  /**
   * getActionsForPod
   * @param params The `ApplicationControllerService.GetActionsForPodUsingGETParams` containing the following parameters:
   *
   * - `podName`: podName
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getActionsForPodUsingGETResponse(params: ApplicationControllerService.GetActionsForPodUsingGETParams): __Observable<__StrictHttpResponse<Array<ApplicationAction>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;




    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/pods/${encodeURIComponent(params.podName)}/actions`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ApplicationAction>>;
      })
    );
  }
  /**
   * getActionsForPod
   * @param params The `ApplicationControllerService.GetActionsForPodUsingGETParams` containing the following parameters:
   *
   * - `podName`: podName
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getActionsForPodUsingGET(params: ApplicationControllerService.GetActionsForPodUsingGETParams): __Observable<Array<ApplicationAction>> {
    return this.getActionsForPodUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<ApplicationAction>)
    );
  }

  /**
   * executeActionOnPod
   * @param params The `ApplicationControllerService.ExecuteActionOnPodUsingPOSTParams` containing the following parameters:
   *
   * - `podName`: podName
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `applicationAction`: applicationAction
   *
   * @return OK
   */
  executeActionOnPodUsingPOSTResponse(params: ApplicationControllerService.ExecuteActionOnPodUsingPOSTParams): __Observable<__StrictHttpResponse<ActionExecution>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;




    __body = params.applicationAction;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/pods/${encodeURIComponent(params.podName)}/actions/executeAction`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ActionExecution>;
      })
    );
  }
  /**
   * executeActionOnPod
   * @param params The `ApplicationControllerService.ExecuteActionOnPodUsingPOSTParams` containing the following parameters:
   *
   * - `podName`: podName
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `applicationAction`: applicationAction
   *
   * @return OK
   */
  executeActionOnPodUsingPOST(params: ApplicationControllerService.ExecuteActionOnPodUsingPOSTParams): __Observable<ActionExecution> {
    return this.executeActionOnPodUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as ActionExecution)
    );
  }

  /**
   * resumeApplication
   * @param params The `ApplicationControllerService.ResumeApplicationUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  resumeApplicationUsingPOSTResponse(params: ApplicationControllerService.ResumeApplicationUsingPOSTParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/resume`,
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
   * resumeApplication
   * @param params The `ApplicationControllerService.ResumeApplicationUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  resumeApplicationUsingPOST(params: ApplicationControllerService.ResumeApplicationUsingPOSTParams): __Observable<boolean> {
    return this.resumeApplicationUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * getApplicationSecrets
   * @param params The `ApplicationControllerService.GetApplicationSecretsUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationSecretsUsingGETResponse(params: ApplicationControllerService.GetApplicationSecretsUsingGETParams): __Observable<__StrictHttpResponse<Array<ApplicationSecret>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/secretRequests`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ApplicationSecret>>;
      })
    );
  }
  /**
   * getApplicationSecrets
   * @param params The `ApplicationControllerService.GetApplicationSecretsUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  getApplicationSecretsUsingGET(params: ApplicationControllerService.GetApplicationSecretsUsingGETParams): __Observable<Array<ApplicationSecret>> {
    return this.getApplicationSecretsUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<ApplicationSecret>)
    );
  }

  /**
   * updateApplicationSecrets
   * @param params The `ApplicationControllerService.UpdateApplicationSecretsUsingPUTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationSecrets`: applicationSecrets
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  updateApplicationSecretsUsingPUTResponse(params: ApplicationControllerService.UpdateApplicationSecretsUsingPUTParams): __Observable<__StrictHttpResponse<Array<ApplicationSecret>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.applicationSecrets;


    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/secrets`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<ApplicationSecret>>;
      })
    );
  }
  /**
   * updateApplicationSecrets
   * @param params The `ApplicationControllerService.UpdateApplicationSecretsUsingPUTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationSecrets`: applicationSecrets
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  updateApplicationSecretsUsingPUT(params: ApplicationControllerService.UpdateApplicationSecretsUsingPUTParams): __Observable<Array<ApplicationSecret>> {
    return this.updateApplicationSecretsUsingPUTResponse(params).pipe(
      __map(_r => _r.body as Array<ApplicationSecret>)
    );
  }

  /**
   * deleteApplicationSecret
   * @param params The `ApplicationControllerService.DeleteApplicationSecretUsingDELETEParams` containing the following parameters:
   *
   * - `secretName`: secretName
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  deleteApplicationSecretUsingDELETEResponse(params: ApplicationControllerService.DeleteApplicationSecretUsingDELETEParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;




    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/applications/${encodeURIComponent(params.applicationId)}/secrets/${encodeURIComponent(params.secretName)}`,
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
   * deleteApplicationSecret
   * @param params The `ApplicationControllerService.DeleteApplicationSecretUsingDELETEParams` containing the following parameters:
   *
   * - `secretName`: secretName
   *
   * - `environment`: environment
   *
   * - `applicationId`: applicationId
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  deleteApplicationSecretUsingDELETE(params: ApplicationControllerService.DeleteApplicationSecretUsingDELETEParams): __Observable<boolean> {
    return this.deleteApplicationSecretUsingDELETEResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * redeploy
   * @param params The `ApplicationControllerService.RedeployUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  redeployUsingPOSTResponse(params: ApplicationControllerService.RedeployUsingPOSTParams): __Observable<__StrictHttpResponse<{[key: string]: boolean}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;


    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${encodeURIComponent(params.applicationFamily)}/${encodeURIComponent(params.environment)}/redeployment`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{[key: string]: boolean}>;
      })
    );
  }
  /**
   * redeploy
   * @param params The `ApplicationControllerService.RedeployUsingPOSTParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationFamily`: applicationFamily
   *
   * @return OK
   */
  redeployUsingPOST(params: ApplicationControllerService.RedeployUsingPOSTParams): __Observable<{[key: string]: boolean}> {
    return this.redeployUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as {[key: string]: boolean})
    );
  }
}

module ApplicationControllerService {

  /**
   * Parameters for upsertApplicationFamilyMetadataUsingPOST
   */
  export interface UpsertApplicationFamilyMetadataUsingPOSTParams {

    /**
     * applicationFamilyMetadata
     */
    applicationFamilyMetadata: ApplicationFamilyMetadata;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for createGenericActionUsingPOST
   */
  export interface CreateGenericActionUsingPOSTParams {

    /**
     * buildType
     */
    buildType: 'MVN' | 'JDK11_MAVEN3' | 'JAVA8_LIBRARY' | 'FREESTYLE_DOCKER' | 'DOTNET_CORE' | 'MVN_IONIC' | 'JDK6_MAVEN2' | 'MJ_NUGET' | 'DOTNET_CORE22' | 'DOTNET_CORE3' | 'SBT' | 'NPM' | 'NPM_UI';

    /**
     * applicationAction
     */
    applicationAction: ApplicationAction;
  }

  /**
   * Parameters for updateUserUsingPUT
   */
  export interface UpdateUserUsingPUTParams {

    /**
     * userId
     */
    userId: string;

    /**
     * user
     */
    user: User;
  }

  /**
   * Parameters for changePasswordUsingPUT
   */
  export interface ChangePasswordUsingPUTParams {

    /**
     * userId
     */
    userId: string;

    /**
     * pwdChange
     */
    pwdChange: PasswordChange;
  }

  /**
   * Parameters for createApplicationUsingPOST
   */
  export interface CreateApplicationUsingPOSTParams {

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

    /**
     * application
     */
    application: Application;
  }

  /**
   * Parameters for updateApplicationUsingPUT
   */
  export interface UpdateApplicationUsingPUTParams {

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

    /**
     * application
     */
    application: Application;
  }

  /**
   * Parameters for getApplicationUsingGET
   */
  export interface GetApplicationUsingGETParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for deleteApplicationUsingDELETE
   */
  export interface DeleteApplicationUsingDELETEParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getApplicationBranchesUsingGET
   */
  export interface GetApplicationBranchesUsingGETParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getBuildsUsingGET
   */
  export interface GetBuildsUsingGETParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for buildUsingPOST
   */
  export interface BuildUsingPOSTParams {

    /**
     * build
     */
    build: Build;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getBuildUsingGET
   */
  export interface GetBuildUsingGETParams {

    /**
     * buildId
     */
    buildId: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for updateBuildUsingPUT
   */
  export interface UpdateBuildUsingPUTParams {

    /**
     * buildId
     */
    buildId: string;

    /**
     * build
     */
    build: Build;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for downloadTestReportUsingGET
   */
  export interface DownloadTestReportUsingGETParams {

    /**
     * buildId
     */
    buildId: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getBuildLogsUsingGET
   */
  export interface GetBuildLogsUsingGETParams {

    /**
     * buildId
     */
    buildId: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

    /**
     * nextToken
     */
    nextToken?: string;
  }

  /**
   * Parameters for getTestBuildDetailsUsingGET
   */
  export interface GetTestBuildDetailsUsingGETParams {

    /**
     * buildId
     */
    buildId: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getExecutedActionsForApplicationUsingGET
   */
  export interface GetExecutedActionsForApplicationUsingGETParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getImagesUsingGET
   */
  export interface GetImagesUsingGETParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getApplicationMetricSummaryUsingGET
   */
  export interface GetApplicationMetricSummaryUsingGETParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getApplicationSecretRequestsUsingGET
   */
  export interface GetApplicationSecretRequestsUsingGETParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for createAppSecretRequestUsingPOST
   */
  export interface CreateAppSecretRequestUsingPOSTParams {

    /**
     * applicationSecretRequests
     */
    applicationSecretRequests: Array<ApplicationSecretRequest>;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getApplicationTagsUsingGET
   */
  export interface GetApplicationTagsUsingGETParams {

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for processWebhookPRBitbucketUsingPOST
   */
  export interface ProcessWebhookPRBitbucketUsingPOSTParams {

    /**
     * webhook
     */
    webhook: BitbucketPREvent;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

    /**
     * X-Event-Key
     */
    XEventKey: string;

    /**
     * Host
     */
    Host: string;
  }

  /**
   * Parameters for processWebhookPRGithubUsingPOST
   */
  export interface ProcessWebhookPRGithubUsingPOSTParams {

    /**
     * webhook
     */
    webhook: GithubPREvent;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

    /**
     * Host
     */
    Host: string;
  }

  /**
   * Parameters for upsertEnvironmentUsingPOST
   */
  export interface UpsertEnvironmentUsingPOSTParams {

    /**
     * environment
     */
    environment: Environment;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getEnvironmentUsingGET
   */
  export interface GetEnvironmentUsingGETParams {

    /**
     * id
     */
    id: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getAlertingDetailsUsingGET
   */
  export interface GetAlertingDetailsUsingGETParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for enableAlertingUsingPOST
   */
  export interface EnableAlertingUsingPOSTParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for disableAlertingUsingDELETE
   */
  export interface DisableAlertingUsingDELETEParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getCurrentDeploymentUsingGET
   */
  export interface GetCurrentDeploymentUsingGETParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getDeploymentStatusUsingGET
   */
  export interface GetDeploymentStatusUsingGETParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for deployUsingPOST
   */
  export interface DeployUsingPOSTParams {

    /**
     * environment
     */
    environment: string;

    /**
     * deployment
     */
    deployment: Deployment;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getDumpFileListUsingGET
   */
  export interface GetDumpFileListUsingGETParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

    /**
     * date
     */
    date?: string;
  }

  /**
   * Parameters for downloadDumpFileUsingGET
   */
  export interface DownloadDumpFileUsingGETParams {

    /**
     * path
     */
    path: string;

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for haltApplicationUsingPOST
   */
  export interface HaltApplicationUsingPOSTParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getMonitoringDetailsUsingGET
   */
  export interface GetMonitoringDetailsUsingGETParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for enableMonitoringUsingPOST
   */
  export interface EnableMonitoringUsingPOSTParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for disableMonitoringUsingDELETE
   */
  export interface DisableMonitoringUsingDELETEParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getApplicationPodDetailsUsingGET
   */
  export interface GetApplicationPodDetailsUsingGETParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getActionsForPodUsingGET
   */
  export interface GetActionsForPodUsingGETParams {

    /**
     * podName
     */
    podName: string;

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for executeActionOnPodUsingPOST
   */
  export interface ExecuteActionOnPodUsingPOSTParams {

    /**
     * podName
     */
    podName: string;

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';

    /**
     * applicationAction
     */
    applicationAction: ApplicationAction;
  }

  /**
   * Parameters for resumeApplicationUsingPOST
   */
  export interface ResumeApplicationUsingPOSTParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for getApplicationSecretsUsingGET
   */
  export interface GetApplicationSecretsUsingGETParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for updateApplicationSecretsUsingPUT
   */
  export interface UpdateApplicationSecretsUsingPUTParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationSecrets
     */
    applicationSecrets: Array<ApplicationSecret>;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for deleteApplicationSecretUsingDELETE
   */
  export interface DeleteApplicationSecretUsingDELETEParams {

    /**
     * secretName
     */
    secretName: string;

    /**
     * environment
     */
    environment: string;

    /**
     * applicationId
     */
    applicationId: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }

  /**
   * Parameters for redeployUsingPOST
   */
  export interface RedeployUsingPOSTParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }
}

export { ApplicationControllerService }
