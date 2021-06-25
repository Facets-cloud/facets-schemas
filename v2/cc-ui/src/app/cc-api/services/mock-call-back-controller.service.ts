/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { CodeBuildStatusCallback } from '../models/code-build-status-callback';

/**
 * Mock Call Back Controller
 */
@Injectable({
  providedIn: 'root',
})
class MockCallBackControllerService extends __BaseService {
  static readonly mockCallBackUsingPOSTPath = '/cc-ui/v1/callbacks/mockcallback';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * mockCallBack
   * @param callback callback
   * @return OK
   */
  mockCallBackUsingPOSTResponse(callback: CodeBuildStatusCallback): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = callback;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/callbacks/mockcallback`,
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
   * mockCallBack
   * @param callback callback
   * @return OK
   */
  mockCallBackUsingPOST(callback: CodeBuildStatusCallback): __Observable<boolean> {
    return this.mockCallBackUsingPOSTResponse(callback).pipe(
      __map(_r => _r.body as boolean)
    );
  }
}

module MockCallBackControllerService {
}

export { MockCallBackControllerService }
