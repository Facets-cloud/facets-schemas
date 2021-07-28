/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { ModelAndView } from '../models/model-and-view';

/**
 * Basic Error Controller
 */
@Injectable({
  providedIn: 'root',
})
class BasicErrorControllerService extends __BaseService {
  static readonly errorHtmlUsingGETPath = '/error';
  static readonly errorHtmlUsingHEADPath = '/error';
  static readonly errorHtmlUsingPOSTPath = '/error';
  static readonly errorHtmlUsingPUTPath = '/error';
  static readonly errorHtmlUsingDELETEPath = '/error';
  static readonly errorHtmlUsingOPTIONSPath = '/error';
  static readonly errorHtmlUsingPATCHPath = '/error';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingGETResponse(): __Observable<__StrictHttpResponse<ModelAndView>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/error`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ModelAndView>;
      })
    );
  }
  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingGET(): __Observable<ModelAndView> {
    return this.errorHtmlUsingGETResponse().pipe(
      __map(_r => _r.body as ModelAndView)
    );
  }

  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingHEADResponse(): __Observable<__StrictHttpResponse<ModelAndView>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'HEAD',
      this.rootUrl + `/error`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ModelAndView>;
      })
    );
  }
  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingHEAD(): __Observable<ModelAndView> {
    return this.errorHtmlUsingHEADResponse().pipe(
      __map(_r => _r.body as ModelAndView)
    );
  }

  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingPOSTResponse(): __Observable<__StrictHttpResponse<ModelAndView>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/error`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ModelAndView>;
      })
    );
  }
  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingPOST(): __Observable<ModelAndView> {
    return this.errorHtmlUsingPOSTResponse().pipe(
      __map(_r => _r.body as ModelAndView)
    );
  }

  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingPUTResponse(): __Observable<__StrictHttpResponse<ModelAndView>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/error`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ModelAndView>;
      })
    );
  }
  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingPUT(): __Observable<ModelAndView> {
    return this.errorHtmlUsingPUTResponse().pipe(
      __map(_r => _r.body as ModelAndView)
    );
  }

  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingDELETEResponse(): __Observable<__StrictHttpResponse<ModelAndView>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/error`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ModelAndView>;
      })
    );
  }
  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingDELETE(): __Observable<ModelAndView> {
    return this.errorHtmlUsingDELETEResponse().pipe(
      __map(_r => _r.body as ModelAndView)
    );
  }

  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingOPTIONSResponse(): __Observable<__StrictHttpResponse<ModelAndView>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'OPTIONS',
      this.rootUrl + `/error`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ModelAndView>;
      })
    );
  }
  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingOPTIONS(): __Observable<ModelAndView> {
    return this.errorHtmlUsingOPTIONSResponse().pipe(
      __map(_r => _r.body as ModelAndView)
    );
  }

  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingPATCHResponse(): __Observable<__StrictHttpResponse<ModelAndView>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'PATCH',
      this.rootUrl + `/error`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ModelAndView>;
      })
    );
  }
  /**
   * errorHtml
   * @return OK
   */
  errorHtmlUsingPATCH(): __Observable<ModelAndView> {
    return this.errorHtmlUsingPATCHResponse().pipe(
      __map(_r => _r.body as ModelAndView)
    );
  }
}

module BasicErrorControllerService {
}

export { BasicErrorControllerService }
