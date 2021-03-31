/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { User } from '../models/user';
import { Role } from '../models/role';

/**
 * Ui User Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiUserControllerService extends __BaseService {
  static readonly getAllUsersUsingGETPath = '/cc-ui/v1/users/';
  static readonly getAllRolesUsingGETPath = '/cc-ui/v1/users/roles';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getAllUsers
   * @return OK
   */
  getAllUsersUsingGETResponse(): __Observable<__StrictHttpResponse<Array<User>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/users/`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<User>>;
      })
    );
  }
  /**
   * getAllUsers
   * @return OK
   */
  getAllUsersUsingGET(): __Observable<Array<User>> {
    return this.getAllUsersUsingGETResponse().pipe(
      __map(_r => _r.body as Array<User>)
    );
  }

  /**
   * getAllRoles
   * @return OK
   */
  getAllRolesUsingGETResponse(): __Observable<__StrictHttpResponse<Array<Role>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/users/roles`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Role>>;
      })
    );
  }
  /**
   * getAllRoles
   * @return OK
   */
  getAllRolesUsingGET(): __Observable<Array<Role>> {
    return this.getAllRolesUsingGETResponse().pipe(
      __map(_r => _r.body as Array<Role>)
    );
  }
}

module UiUserControllerService {
}

export { UiUserControllerService }
