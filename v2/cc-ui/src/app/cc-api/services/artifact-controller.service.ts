/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { Artifact } from '../models/artifact';

/**
 * Artifact Controller
 */
@Injectable({
  providedIn: 'root',
})
class ArtifactControllerService extends __BaseService {
  static readonly registerArtifactUsingPOSTPath = '/cc/v1/artifacts/register';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * registerArtifact
   * @param artifact artifact
   */
  registerArtifactUsingPOSTResponse(artifact: Artifact): __Observable<__StrictHttpResponse<null>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = artifact;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc/v1/artifacts/register`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<null>;
      })
    );
  }
  /**
   * registerArtifact
   * @param artifact artifact
   */
  registerArtifactUsingPOST(artifact: Artifact): __Observable<null> {
    return this.registerArtifactUsingPOSTResponse(artifact).pipe(
      __map(_r => _r.body as null)
    );
  }
}

module ArtifactControllerService {
}

export { ArtifactControllerService }
