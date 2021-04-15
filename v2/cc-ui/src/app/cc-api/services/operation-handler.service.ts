/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';


/**
 * Operation Handler
 */
@Injectable({
  providedIn: 'root',
})
class OperationHandlerService extends __BaseService {
  static readonly handleUsingGET2Path = '/actuator/health';
  static readonly handleUsingGET1Path = '/actuator/health/{component}';
  static readonly handleUsingGETPath = '/actuator/health/{component}/{instance}';
  static readonly handleUsingGET3Path = '/actuator/info';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * handle
   * @param body body
   * @return OK
   */
  handleUsingGET2Response(body?: {[key: string]: string}): __Observable<__StrictHttpResponse<{}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = body;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/actuator/health`,
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
   * handle
   * @param body body
   * @return OK
   */
  handleUsingGET2(body?: {[key: string]: string}): __Observable<{}> {
    return this.handleUsingGET2Response(body).pipe(
      __map(_r => _r.body as {})
    );
  }

  /**
   * handle
   * @param body body
   * @return OK
   */
  handleUsingGET1Response(body?: {[key: string]: string}): __Observable<__StrictHttpResponse<{}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = body;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/actuator/health/`,
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
   * handle
   * @param body body
   * @return OK
   */
  handleUsingGET1(body?: {[key: string]: string}): __Observable<{}> {
    return this.handleUsingGET1Response(body).pipe(
      __map(_r => _r.body as {})
    );
  }

  /**
   * handle
   * @param body body
   * @return OK
   */
  handleUsingGETResponse(body?: {[key: string]: string}): __Observable<__StrictHttpResponse<{}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = body;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/actuator/health/`,
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
   * handle
   * @param body body
   * @return OK
   */
  handleUsingGET(body?: {[key: string]: string}): __Observable<{}> {
    return this.handleUsingGETResponse(body).pipe(
      __map(_r => _r.body as {})
    );
  }

  /**
   * handle
   * @param body body
   * @return OK
   */
  handleUsingGET3Response(body?: {[key: string]: string}): __Observable<__StrictHttpResponse<{}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = body;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/actuator/info`,
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
   * handle
   * @param body body
   * @return OK
   */
  handleUsingGET3(body?: {[key: string]: string}): __Observable<{}> {
    return this.handleUsingGET3Response(body).pipe(
      __map(_r => _r.body as {})
    );
  }
}

module OperationHandlerService {
}

export { OperationHandlerService }
