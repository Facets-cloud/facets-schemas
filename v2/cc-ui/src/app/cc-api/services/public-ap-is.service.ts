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
 * Public AP Is
 */
@Injectable({
  providedIn: 'root',
})
class PublicApIsService extends __BaseService {
  static readonly getLoginOptionsUsingGETPath = '/public/v1/loginOptions';
  static readonly getLogoUsingGETPath = '/public/v1/logo';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getLoginOptions
   * @return OK
   */
  getLoginOptionsUsingGETResponse(): __Observable<__StrictHttpResponse<Array<CustomOAuth2ClientRegistration>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/public/v1/loginOptions`,
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
   * getLoginOptions
   * @return OK
   */
  getLoginOptionsUsingGET(): __Observable<Array<CustomOAuth2ClientRegistration>> {
    return this.getLoginOptionsUsingGETResponse().pipe(
      __map(_r => _r.body as Array<CustomOAuth2ClientRegistration>)
    );
  }

  /**
   * getLogo
   * @return OK
   */
  getLogoUsingGETResponse(): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/public/v1/logo`,
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
   * getLogo
   * @return OK
   */
  getLogoUsingGET(): __Observable<string> {
    return this.getLogoUsingGETResponse().pipe(
      __map(_r => _r.body as string)
    );
  }
}

module PublicApIsService {
}

export { PublicApIsService }
