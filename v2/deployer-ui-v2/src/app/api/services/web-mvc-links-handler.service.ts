/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { Link } from '../models/link';

/**
 * Web Mvc Links Handler
 */
@Injectable({
  providedIn: 'root',
})
class WebMvcLinksHandlerService extends __BaseService {
  static readonly linksUsingGETPath = '/actuator';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @return OK
   */
  linksUsingGETResponse(): __Observable<__StrictHttpResponse<{[key: string]: {[key: string]: Link}}>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/actuator`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<{[key: string]: {[key: string]: Link}}>;
      })
    );
  }
  /**
   * @return OK
   */
  linksUsingGET(): __Observable<{[key: string]: {[key: string]: Link}}> {
    return this.linksUsingGETResponse().pipe(
      __map(_r => _r.body as {[key: string]: {[key: string]: Link}})
    );
  }
}

module WebMvcLinksHandlerService {
}

export { WebMvcLinksHandlerService }
