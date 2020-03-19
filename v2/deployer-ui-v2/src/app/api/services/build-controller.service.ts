/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';


/**
 * Build Controller
 */
@Injectable({
  providedIn: 'root',
})
class BuildControllerService extends __BaseService {
  static readonly getImageFromDeployerUsingGETPath = '/cc/v1/build/deployer/{applicationId}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @param params The `BuildControllerService.GetImageFromDeployerUsingGETParams` containing the following parameters:
   *
   * - `strategy`: strategy
   *
   * - `applicationId`: applicationId
   *
   * @return OK
   */
  getImageFromDeployerUsingGETResponse(params: BuildControllerService.GetImageFromDeployerUsingGETParams): __Observable<__StrictHttpResponse<string>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    if (params.strategy != null) __params = __params.set('strategy', params.strategy.toString());

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/build/deployer/${params.applicationId}`,
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
   * @param params The `BuildControllerService.GetImageFromDeployerUsingGETParams` containing the following parameters:
   *
   * - `strategy`: strategy
   *
   * - `applicationId`: applicationId
   *
   * @return OK
   */
  getImageFromDeployerUsingGET(params: BuildControllerService.GetImageFromDeployerUsingGETParams): __Observable<string> {
    return this.getImageFromDeployerUsingGETResponse(params).pipe(
      __map(_r => _r.body as string)
    );
  }
}

module BuildControllerService {

  /**
   * Parameters for getImageFromDeployerUsingGET
   */
  export interface GetImageFromDeployerUsingGETParams {

    /**
     * strategy
     */
    strategy: 'QA' | 'STAGING' | 'PROD';

    /**
     * applicationId
     */
    applicationId: string;
  }
}

export { BuildControllerService }
