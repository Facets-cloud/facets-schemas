/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { User } from '../models/user';
import { Application } from '../models/application';
import { Build } from '../models/build';
import { TokenPaginatedResponseLogEvent } from '../models/token-paginated-response-log-event';
import { DeploymentStatusDetails } from '../models/deployment-status-details';
import { Deployment } from '../models/deployment';
import { ApplicationSecret } from '../models/application-secret';
import { InputStreamResource } from '../models/input-stream-resource';

/**
 * Application Controller
 */
@Injectable({
  providedIn: 'root',
})
class ApplicationControllerService extends __BaseService {
  static readonly getApplicationFamiliesUsingGETPath = '/api/applicationFamilies';
  static readonly getUsersUsingGETPath = '/api/users';
  static readonly createUserUsingPOSTPath = '/api/users';
  static readonly updateUserUsingPUTPath = '/api/users/{userId}';
  static readonly getApplicationsUsingGETPath = '/api/{applicationFamily}/applications';
  static readonly createApplicationUsingPOSTPath = '/api/{applicationFamily}/applications';
  static readonly getApplicationUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}';
  static readonly getBuildsUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/builds';
  static readonly buildUsingPOSTPath = '/api/{applicationFamily}/applications/{applicationId}/builds';
  static readonly getBuildUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/builds/{buildId}';
  static readonly getBuildLogsUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/builds/{buildId}/logs';
  static readonly getImagesUsingGETPath = '/api/{applicationFamily}/applications/{applicationId}/images';
  static readonly getDeploymentStatusUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/deploymentStatus';
  static readonly deployUsingPOSTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/deployments';
  static readonly getApplicationSecretsUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/secrets';
  static readonly initializeApplicationSecretsUsingPOSTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/secrets';
  static readonly updateApplicationSecretsUsingPUTPath = '/api/{applicationFamily}/{environment}/applications/{applicationId}/secrets';
  static readonly getDumpFileListUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationName}/dumps';
  static readonly downloadDumpFileUsingGETPath = '/api/{applicationFamily}/{environment}/applications/{applicationName}/dumps/download';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
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
   * @return OK
   */
  getApplicationFamiliesUsingGET(): __Observable<Array<'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>> {
    return this.getApplicationFamiliesUsingGETResponse().pipe(
      __map(_r => _r.body as Array<'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>)
    );
  }

  /**
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
   * @return OK
   */
  getUsersUsingGET(): __Observable<Array<User>> {
    return this.getUsersUsingGETResponse().pipe(
      __map(_r => _r.body as Array<User>)
    );
  }

  /**
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
   * @param user user
   * @return OK
   */
  createUserUsingPOST(user: User): __Observable<User> {
    return this.createUserUsingPOSTResponse(user).pipe(
      __map(_r => _r.body as User)
    );
  }

  /**
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
      this.rootUrl + `/api/users/${params.userId}`,
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
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getApplicationsUsingGETResponse(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<__StrictHttpResponse<Array<Application>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${applicationFamily}/applications`,
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
   * @param applicationFamily applicationFamily
   * @return OK
   */
  getApplicationsUsingGET(applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): __Observable<Array<Application>> {
    return this.getApplicationsUsingGETResponse(applicationFamily).pipe(
      __map(_r => _r.body as Array<Application>)
    );
  }

  /**
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
      this.rootUrl + `/api/${params.applicationFamily}/applications`,
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
      this.rootUrl + `/api/${params.applicationFamily}/applications/${params.applicationId}`,
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
      this.rootUrl + `/api/${params.applicationFamily}/applications/${params.applicationId}/builds`,
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
      this.rootUrl + `/api/${params.applicationFamily}/applications/${params.applicationId}/builds`,
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
      this.rootUrl + `/api/${params.applicationFamily}/applications/${params.applicationId}/builds/${params.buildId}`,
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
      this.rootUrl + `/api/${params.applicationFamily}/applications/${params.applicationId}/builds/${params.buildId}/logs`,
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
      this.rootUrl + `/api/${params.applicationFamily}/applications/${params.applicationId}/images`,
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
      this.rootUrl + `/api/${params.applicationFamily}/${params.environment}/applications/${params.applicationId}/deploymentStatus`,
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
      this.rootUrl + `/api/${params.applicationFamily}/${params.environment}/applications/${params.applicationId}/deployments`,
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
      this.rootUrl + `/api/${params.applicationFamily}/${params.environment}/applications/${params.applicationId}/secrets`,
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
   * @param params The `ApplicationControllerService.InitializeApplicationSecretsUsingPOSTParams` containing the following parameters:
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
  initializeApplicationSecretsUsingPOSTResponse(params: ApplicationControllerService.InitializeApplicationSecretsUsingPOSTParams): __Observable<__StrictHttpResponse<Array<ApplicationSecret>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.applicationSecrets;


    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/api/${params.applicationFamily}/${params.environment}/applications/${params.applicationId}/secrets`,
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
   * @param params The `ApplicationControllerService.InitializeApplicationSecretsUsingPOSTParams` containing the following parameters:
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
  initializeApplicationSecretsUsingPOST(params: ApplicationControllerService.InitializeApplicationSecretsUsingPOSTParams): __Observable<Array<ApplicationSecret>> {
    return this.initializeApplicationSecretsUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Array<ApplicationSecret>)
    );
  }

  /**
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
      this.rootUrl + `/api/${params.applicationFamily}/${params.environment}/applications/${params.applicationId}/secrets`,
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
   * @param params The `ApplicationControllerService.GetDumpFileListUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationName`: applicationName
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `date`: date
   *
   * @return OK
   */
  getDumpFileListUsingGETResponse(params: ApplicationControllerService.GetDumpFileListUsingGETParams): __Observable<__StrictHttpResponse<Array<string>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;



    if (params.date != null) __params = __params.set('date', params.date.toString());
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/api/${params.applicationFamily}/${params.environment}/applications/${params.applicationName}/dumps`,
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
   * @param params The `ApplicationControllerService.GetDumpFileListUsingGETParams` containing the following parameters:
   *
   * - `environment`: environment
   *
   * - `applicationName`: applicationName
   *
   * - `applicationFamily`: applicationFamily
   *
   * - `date`: date
   *
   * @return OK
   */
  getDumpFileListUsingGET(params: ApplicationControllerService.GetDumpFileListUsingGETParams): __Observable<Array<string>> {
    return this.getDumpFileListUsingGETResponse(params).pipe(
      __map(_r => _r.body as Array<string>)
    );
  }

  /**
   * @param params The `ApplicationControllerService.DownloadDumpFileUsingGETParams` containing the following parameters:
   *
   * - `path`: path
   *
   * - `environment`: environment
   *
   * - `applicationName`: applicationName
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
      this.rootUrl + `/api/${params.applicationFamily}/${params.environment}/applications/${params.applicationName}/dumps/download`,
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
   * @param params The `ApplicationControllerService.DownloadDumpFileUsingGETParams` containing the following parameters:
   *
   * - `path`: path
   *
   * - `environment`: environment
   *
   * - `applicationName`: applicationName
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
}

module ApplicationControllerService {

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
   * Parameters for initializeApplicationSecretsUsingPOST
   */
  export interface InitializeApplicationSecretsUsingPOSTParams {

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
   * Parameters for getDumpFileListUsingGET
   */
  export interface GetDumpFileListUsingGETParams {

    /**
     * environment
     */
    environment: string;

    /**
     * applicationName
     */
    applicationName: string;

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
     * applicationName
     */
    applicationName: string;

    /**
     * applicationFamily
     */
    applicationFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  }
}

export { ApplicationControllerService }
