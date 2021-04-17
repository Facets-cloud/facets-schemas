/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { SupportedVersions } from '../models/supported-versions';

/**
 * Meta Controller
 */
@Injectable({
  providedIn: 'root',
})
class MetaControllerService extends __BaseService {
  static readonly getSupportedComponentVersionsUsingGETPath = '/cc/v1/meta/components/supportedVersion';
  static readonly getSupportedComponentVersionUsingGETPath = '/cc/v1/meta/components/{componentType}/supportedVersion';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getSupportedComponentVersions
   * @return OK
   */
  getSupportedComponentVersionsUsingGETResponse(): __Observable<__StrictHttpResponse<Array<SupportedVersions>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/meta/components/supportedVersion`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<SupportedVersions>>;
      })
    );
  }
  /**
   * getSupportedComponentVersions
   * @return OK
   */
  getSupportedComponentVersionsUsingGET(): __Observable<Array<SupportedVersions>> {
    return this.getSupportedComponentVersionsUsingGETResponse().pipe(
      __map(_r => _r.body as Array<SupportedVersions>)
    );
  }

  /**
   * getSupportedComponentVersion
   * @param componentType componentType
   * @return OK
   */
  getSupportedComponentVersionUsingGETResponse(componentType: 'KUBERNETES'): __Observable<__StrictHttpResponse<SupportedVersions>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/meta/components/${encodeURIComponent(componentType)}/supportedVersion`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<SupportedVersions>;
      })
    );
  }
  /**
   * getSupportedComponentVersion
   * @param componentType componentType
   * @return OK
   */
  getSupportedComponentVersionUsingGET(componentType: 'KUBERNETES'): __Observable<SupportedVersions> {
    return this.getSupportedComponentVersionUsingGETResponse(componentType).pipe(
      __map(_r => _r.body as SupportedVersions)
    );
  }
}

module MetaControllerService {
}

export { MetaControllerService }
