/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { Team } from '../models/team';
import { TeamMembership } from '../models/team-membership';

/**
 * Ui Team Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiTeamControllerService extends __BaseService {
  static readonly getTeamsUsingGETPath = '/cc-ui/v1/teams/';
  static readonly upsertTeamUsingPOSTPath = '/cc-ui/v1/teams/';
  static readonly getTeamUsingGETPath = '/cc-ui/v1/teams/{teamId}';
  static readonly getTeamMembersUsingGETPath = '/cc-ui/v1/teams/{teamId}/members';
  static readonly addTeamMembersUsingPOSTPath = '/cc-ui/v1/teams/{teamId}/members';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @return OK
   */
  getTeamsUsingGETResponse(): __Observable<__StrictHttpResponse<Array<Team>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/teams/`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Team>>;
      })
    );
  }
  /**
   * @return OK
   */
  getTeamsUsingGET(): __Observable<Array<Team>> {
    return this.getTeamsUsingGETResponse().pipe(
      __map(_r => _r.body as Array<Team>)
    );
  }

  /**
   * @param team team
   * @return OK
   */
  upsertTeamUsingPOSTResponse(team: Team): __Observable<__StrictHttpResponse<Team>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = team;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/teams/`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Team>;
      })
    );
  }
  /**
   * @param team team
   * @return OK
   */
  upsertTeamUsingPOST(team: Team): __Observable<Team> {
    return this.upsertTeamUsingPOSTResponse(team).pipe(
      __map(_r => _r.body as Team)
    );
  }

  /**
   * @param teamId teamId
   * @return OK
   */
  getTeamUsingGETResponse(teamId: string): __Observable<__StrictHttpResponse<Team>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/teams/${teamId}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Team>;
      })
    );
  }
  /**
   * @param teamId teamId
   * @return OK
   */
  getTeamUsingGET(teamId: string): __Observable<Team> {
    return this.getTeamUsingGETResponse(teamId).pipe(
      __map(_r => _r.body as Team)
    );
  }

  /**
   * @param teamId teamId
   * @return OK
   */
  getTeamMembersUsingGETResponse(teamId: string): __Observable<__StrictHttpResponse<Array<TeamMembership>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/teams/${teamId}/members`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<TeamMembership>>;
      })
    );
  }
  /**
   * @param teamId teamId
   * @return OK
   */
  getTeamMembersUsingGET(teamId: string): __Observable<Array<TeamMembership>> {
    return this.getTeamMembersUsingGETResponse(teamId).pipe(
      __map(_r => _r.body as Array<TeamMembership>)
    );
  }

  /**
   * @param params The `UiTeamControllerService.AddTeamMembersUsingPOSTParams` containing the following parameters:
   *
   * - `userNames`: userNames
   *
   * - `teamId`: teamId
   *
   * @return OK
   */
  addTeamMembersUsingPOSTResponse(params: UiTeamControllerService.AddTeamMembersUsingPOSTParams): __Observable<__StrictHttpResponse<Array<TeamMembership>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.userNames;

    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/teams/${params.teamId}/members`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<TeamMembership>>;
      })
    );
  }
  /**
   * @param params The `UiTeamControllerService.AddTeamMembersUsingPOSTParams` containing the following parameters:
   *
   * - `userNames`: userNames
   *
   * - `teamId`: teamId
   *
   * @return OK
   */
  addTeamMembersUsingPOST(params: UiTeamControllerService.AddTeamMembersUsingPOSTParams): __Observable<Array<TeamMembership>> {
    return this.addTeamMembersUsingPOSTResponse(params).pipe(
      __map(_r => _r.body as Array<TeamMembership>)
    );
  }
}

module UiTeamControllerService {

  /**
   * Parameters for addTeamMembersUsingPOST
   */
  export interface AddTeamMembersUsingPOSTParams {

    /**
     * userNames
     */
    userNames: Array<string>;

    /**
     * teamId
     */
    teamId: string;
  }
}

export { UiTeamControllerService }
