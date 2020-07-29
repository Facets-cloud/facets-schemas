/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { CallbackBody } from '../models/callback-body';

/**
 * Callback Controller
 */
@Injectable({
  providedIn: 'root',
})
class CallbackControllerService extends __BaseService {
  static readonly sonarCallBackUsingPOSTPath = '/callback/sonar';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @param body body
   * @return OK
   */
  sonarCallBackUsingPOSTResponse(body: CallbackBody): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = body;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/callback/sonar`,
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
   * @param body body
   * @return OK
   */
  sonarCallBackUsingPOST(body: CallbackBody): __Observable<boolean> {
    return this.sonarCallBackUsingPOSTResponse(body).pipe(
      __map(_r => _r.body as boolean)
    );
  }
}

module CallbackControllerService {
}

export { CallbackControllerService }
