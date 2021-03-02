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
 * Artifactory Controller
 */
@Injectable({
  providedIn: 'root',
})
class ArtifactoryControllerService extends __BaseService {
  static readonly getAllArtifactoriesUsingGETPath = '/cc/v1/artifactories';
  static readonly createECRArtifactoryUsingPOSTPath = '/cc/v1/artifactories';
  static readonly updateECRArtifactoryUsingPUTPath = '/cc/v1/artifactories/{artifactoryId}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @return OK
   */
  getAllArtifactoriesUsingGETResponse(): __Observable<__StrictHttpResponse<Array<Artifactory>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc/v1/artifactories`,
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
   * @return OK
   */
  getAllArtifactoriesUsingGET(): __Observable<Array<Artifactory>> {
    return this.getAllArtifactoriesUsingGETResponse().pipe(
      __map(_r => _r.body as Array<Artifactory>)
    );
  }

  /**
   * @param ecrArtifactory ecrArtifactory
   * @return OK
   */
  createECRArtifactoryUsingPOSTResponse(ecrArtifactory: ECRArtifactory): __Observable<__StrictHttpResponse<ECRArtifactory>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = ecrArtifactory;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/artifactories`,
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
   * @param ecrArtifactory ecrArtifactory
   * @return OK
   */
  createECRArtifactoryUsingPOST(ecrArtifactory: ECRArtifactory): __Observable<ECRArtifactory> {
    return this.createECRArtifactoryUsingPOSTResponse(ecrArtifactory).pipe(
      __map(_r => _r.body as ECRArtifactory)
    );
  }

  /**
   * @param params The `ArtifactoryControllerService.UpdateECRArtifactoryUsingPUTParams` containing the following parameters:
   *
   * - `ecrArtifactory`: ecrArtifactory
   *
   * - `artifactoryId`: artifactoryId
   *
   * @return OK
   */
  updateECRArtifactoryUsingPUTResponse(params: ArtifactoryControllerService.UpdateECRArtifactoryUsingPUTParams): __Observable<__StrictHttpResponse<ECRArtifactory>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.ecrArtifactory;

    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc/v1/artifactories/${params.artifactoryId}`,
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
   * @param params The `ArtifactoryControllerService.UpdateECRArtifactoryUsingPUTParams` containing the following parameters:
   *
   * - `ecrArtifactory`: ecrArtifactory
   *
   * - `artifactoryId`: artifactoryId
   *
   * @return OK
   */
  updateECRArtifactoryUsingPUT(params: ArtifactoryControllerService.UpdateECRArtifactoryUsingPUTParams): __Observable<ECRArtifactory> {
    return this.updateECRArtifactoryUsingPUTResponse(params).pipe(
      __map(_r => _r.body as ECRArtifactory)
    );
  }
}

module ArtifactoryControllerService {

  /**
   * Parameters for updateECRArtifactoryUsingPUT
   */
  export interface UpdateECRArtifactoryUsingPUTParams {

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

export { ArtifactoryControllerService }
