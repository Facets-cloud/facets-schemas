/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { Artifactory } from '../models/artifactory';
import { ECRArtifactory } from '../models/ecrartifactory';

/**
 * Ui Artifactory Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiArtifactoryControllerService extends __BaseService {
  static readonly getAllArtifactoriesUsingGET1Path = '/cc-ui/v1/artifactories';
  static readonly createECRArtifactoryUsingPOST1Path = '/cc-ui/v1/artifactories';
  static readonly updateECRArtifactoryUsingPOSTPath = '/cc-ui/v1/artifactories/{artifactoryId}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getAllArtifactories
   * @return OK
   */
  getAllArtifactoriesUsingGET1Response(): __Observable<__StrictHttpResponse<Array<Artifactory>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/artifactories`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Artifactory>>;
      })
    );
  }
  /**
   * getAllArtifactories
   * @return OK
   */
  getAllArtifactoriesUsingGET1(): __Observable<Array<Artifactory>> {
    return this.getAllArtifactoriesUsingGET1Response().pipe(
      __map(_r => _r.body as Array<Artifactory>)
    );
  }

  /**
   * createECRArtifactory
   * @param ecrArtifactory ecrArtifactory
   * @return OK
   */
  createECRArtifactoryUsingPOST1Response(ecrArtifactory: ECRArtifactory): __Observable<__StrictHttpResponse<ECRArtifactory>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = ecrArtifactory;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/artifactories`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ECRArtifactory>;
      })
    );
  }
  /**
   * createECRArtifactory
   * @param ecrArtifactory ecrArtifactory
   * @return OK
   */
  createECRArtifactoryUsingPOST1(ecrArtifactory: ECRArtifactory): __Observable<ECRArtifactory> {
    return this.createECRArtifactoryUsingPOST1Response(ecrArtifactory).pipe(
      __map(_r => _r.body as ECRArtifactory)
    );
  }

  /**
   * updateECRArtifactory
   * @param params The `UiArtifactoryControllerService.UpdateECRArtifactoryUsingPOSTParams` containing the following parameters:
   *
   * - `ecrArtifactory`: ecrArtifactory
   *
   * - `artifactoryId`: artifactoryId
   *
   * @return OK
   */
  updateECRArtifactoryUsingPOSTResponse(params: UiArtifactoryControllerService.UpdateECRArtifactoryUsingPOSTParams): __Observable<__StrictHttpResponse<ECRArtifactory>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.ecrArtifactory;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/artifactories/${encodeURIComponent(params.artifactoryId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<ECRArtifactory>;
      })
    );
  }
  /**
   * updateECRArtifactory
   * @param params The `UiArtifactoryControllerService.UpdateECRArtifactoryUsingPOSTParams` containing the following parameters:
   *
   * - `ecrArtifactory`: ecrArtifactory
   *
   * - `artifactoryId`: artifactoryId
   *
   * @return OK
   */
  updateECRArtifactoryUsingPOST(params: UiArtifactoryControllerService.UpdateECRArtifactoryUsingPOSTParams): __Observable<ECRArtifactory> {
    return this.updateECRArtifactoryUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as ECRArtifactory)
    );
  }
}

module UiArtifactoryControllerService {

  /**
   * Parameters for updateECRArtifactoryUsingPOST
   */
  export interface UpdateECRArtifactoryUsingPOSTParams {

    /**
     * ecrArtifactory
     */
    ecrArtifactory: ECRArtifactory;

    /**
     * artifactoryId
     */
    artifactoryId: string;
  }
}

export { UiArtifactoryControllerService }
