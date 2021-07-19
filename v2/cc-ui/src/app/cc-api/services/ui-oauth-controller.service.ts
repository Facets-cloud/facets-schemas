/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { CustomOAuth2ClientRegistration } from '../models/custom-oauth-2client-registration';

/**
 * Ui O Auth Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiOAuthControllerService extends __BaseService {
  static readonly getAllIntegrationsUsingGETPath = '/cc-ui/v1/oauth';
  static readonly addIntegrationsUsingPOSTPath = '/cc-ui/v1/oauth';
  static readonly updateIntegrationsUsingPUTPath = '/cc-ui/v1/oauth/{registrationId}';
  static readonly deleteIntegrationsUsingDELETEPath = '/cc-ui/v1/oauth/{registrationId}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getAllIntegrations
   * @return OK
   */
  getAllIntegrationsUsingGETResponse(): __Observable<__StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/oauth`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>;
      })
    );
  }
  /**
   * getAllIntegrations
   * @return OK
   */
  getAllIntegrationsUsingGET(): __Observable<Array<CustomOAuth2ClientRegistration>> {
    return this.getAllIntegrationsUsingGETResponse().pipe(
      __map(_r => _r.body as Array<CustomOAuth2ClientRegistration>)
    );
  }

  /**
   * addIntegrations
   * @param client client
   * @return OK
   */
  addIntegrationsUsingPOSTResponse(client: CustomOAuth2ClientRegistration): __Observable<__StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = client;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/oauth`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>;
      })
    );
  }
  /**
   * addIntegrations
   * @param client client
   * @return OK
   */
  addIntegrationsUsingPOST(client: CustomOAuth2ClientRegistration): __Observable<Array<CustomOAuth2ClientRegistration>> {
    return this.addIntegrationsUsingPOSTResponse(client).pipe(
      __map(_r => _r.body as Array<CustomOAuth2ClientRegistration>)
    );
  }

  /**
   * updateIntegrations
   * @param params The `UiOAuthControllerService.UpdateIntegrationsUsingPUTParams` containing the following parameters:
   *
   * - `registrationId`: registrationId
   *
   * - `client`: client
   *
   * @return OK
   */
  updateIntegrationsUsingPUTResponse(params: UiOAuthControllerService.UpdateIntegrationsUsingPUTParams): __Observable<__StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.client;
    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc-ui/v1/oauth/${encodeURIComponent(params.registrationId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>;
      })
    );
  }
  /**
   * updateIntegrations
   * @param params The `UiOAuthControllerService.UpdateIntegrationsUsingPUTParams` containing the following parameters:
   *
   * - `registrationId`: registrationId
   *
   * - `client`: client
   *
   * @return OK
   */
  updateIntegrationsUsingPUT(params: UiOAuthControllerService.UpdateIntegrationsUsingPUTParams): __Observable<Array<CustomOAuth2ClientRegistration>> {
    return this.updateIntegrationsUsingPUTResponse(params).pipe(
      __map(_r => _r.body as Array<CustomOAuth2ClientRegistration>)
    );
  }

  /**
   * deleteIntegrations
   * @param registrationId registrationId
   * @return OK
   */
  deleteIntegrationsUsingDELETEResponse(registrationId: string): __Observable<__StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/cc-ui/v1/oauth/${encodeURIComponent(registrationId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>;
      })
    );
  }
  /**
   * deleteIntegrations
   * @param registrationId registrationId
   * @return OK
   */
  deleteIntegrationsUsingDELETE(registrationId: string): __Observable<Array<CustomOAuth2ClientRegistration>> {
    return this.deleteIntegrationsUsingDELETEResponse(registrationId).pipe(
      __map(_r => _r.body as Array<CustomOAuth2ClientRegistration>)
    );
  }
}

module UiOAuthControllerService {

  /**
   * Parameters for updateIntegrationsUsingPUT
   */
  export interface UpdateIntegrationsUsingPUTParams {

    /**
     * registrationId
     */
    registrationId: string;

    /**
     * client
     */
    client: CustomOAuth2ClientRegistration;
  }
}

export { UiOAuthControllerService }
