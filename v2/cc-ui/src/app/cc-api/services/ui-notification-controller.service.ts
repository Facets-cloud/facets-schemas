/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { ApiConfiguration as __Configuration } from '../api-configuration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { NotificationChannel } from '../models/notification-channel';
import { Subscription } from '../models/subscription';

/**
 * Ui Notification Controller
 */
@Injectable({
  providedIn: 'root',
})
class UiNotificationControllerService extends __BaseService {
  static readonly getAllChannelTypesUsingGETPath = '/cc-ui/v1/notification/channelTypes';
  static readonly getAllChannelsUsingGETPath = '/cc-ui/v1/notification/channels';
  static readonly createNotificationChannelUsingPOSTPath = '/cc-ui/v1/notification/channels';
  static readonly testNotificationChannelUsingPOSTPath = '/cc-ui/v1/notification/channels/test';
  static readonly editNotificationChannelUsingPUTPath = '/cc-ui/v1/notification/channels/{channelId}';
  static readonly deleteNotificationChannelUsingDELETEPath = '/cc-ui/v1/notification/channels/{channelId}';
  static readonly getAllNotificationTypesUsingGETPath = '/cc-ui/v1/notification/notificationTypes';
  static readonly getAllSubscriptionsUsingGETPath = '/cc-ui/v1/notification/subscriptions';
  static readonly createSubscriptionUsingPOSTPath = '/cc-ui/v1/notification/subscriptions';
  static readonly editSubscriptionUsingPUTPath = '/cc-ui/v1/notification/subscriptions/{subscriptionId}';
  static readonly deleteSubscriptionUsingDELETEPath = '/cc-ui/v1/notification/subscriptions/{subscriptionId}';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * getAllChannelTypes
   * @return OK
   */
  getAllChannelTypesUsingGETResponse(): __Observable<__StrictHttpResponse<Array<'FLOCK' | 'SLACK'>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/notification/channelTypes`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<'FLOCK' | 'SLACK'>>;
      })
    );
  }
  /**
   * getAllChannelTypes
   * @return OK
   */
  getAllChannelTypesUsingGET(): __Observable<Array<'FLOCK' | 'SLACK'>> {
    return this.getAllChannelTypesUsingGETResponse().pipe(
      __map(_r => _r.body as Array<'FLOCK' | 'SLACK'>)
    );
  }

  /**
   * getAllChannels
   * @return OK
   */
  getAllChannelsUsingGETResponse(): __Observable<__StrictHttpResponse<Array<NotificationChannel>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/notification/channels`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<NotificationChannel>>;
      })
    );
  }
  /**
   * getAllChannels
   * @return OK
   */
  getAllChannelsUsingGET(): __Observable<Array<NotificationChannel>> {
    return this.getAllChannelsUsingGETResponse().pipe(
      __map(_r => _r.body as Array<NotificationChannel>)
    );
  }

  /**
   * createNotificationChannel
   * @param nc nc
   * @return OK
   */
  createNotificationChannelUsingPOSTResponse(nc: NotificationChannel): __Observable<__StrictHttpResponse<Array<NotificationChannel>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = nc;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/notification/channels`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<NotificationChannel>>;
      })
    );
  }
  /**
   * createNotificationChannel
   * @param nc nc
   * @return OK
   */
  createNotificationChannelUsingPOST(nc: NotificationChannel): __Observable<Array<NotificationChannel>> {
    return this.createNotificationChannelUsingPOSTResponse(nc).pipe(
      __map(_r => _r.body as Array<NotificationChannel>)
    );
  }

  /**
   * testNotificationChannel
   * @param nc nc
   * @return OK
   */
  testNotificationChannelUsingPOSTResponse(nc: NotificationChannel): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = nc;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/notification/channels/test`,
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
   * testNotificationChannel
   * @param nc nc
   * @return OK
   */
  testNotificationChannelUsingPOST(nc: NotificationChannel): __Observable<boolean> {
    return this.testNotificationChannelUsingPOSTResponse(nc).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * editNotificationChannel
   * @param params The `UiNotificationControllerService.EditNotificationChannelUsingPUTParams` containing the following parameters:
   *
   * - `nc`: nc
   *
   * - `channelId`: channelId
   *
   * @return OK
   */
  editNotificationChannelUsingPUTResponse(params: UiNotificationControllerService.EditNotificationChannelUsingPUTParams): __Observable<__StrictHttpResponse<Array<NotificationChannel>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = params.nc;

    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc-ui/v1/notification/channels/${encodeURIComponent(params.channelId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<NotificationChannel>>;
      })
    );
  }
  /**
   * editNotificationChannel
   * @param params The `UiNotificationControllerService.EditNotificationChannelUsingPUTParams` containing the following parameters:
   *
   * - `nc`: nc
   *
   * - `channelId`: channelId
   *
   * @return OK
   */
  editNotificationChannelUsingPUT(params: UiNotificationControllerService.EditNotificationChannelUsingPUTParams): __Observable<Array<NotificationChannel>> {
    return this.editNotificationChannelUsingPUTResponse(params).pipe(
      __map(_r => _r.body as Array<NotificationChannel>)
    );
  }

  /**
   * deleteNotificationChannel
   * @param channelId channelId
   * @return OK
   */
  deleteNotificationChannelUsingDELETEResponse(channelId: string): __Observable<__StrictHttpResponse<Array<NotificationChannel>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/cc-ui/v1/notification/channels/${encodeURIComponent(channelId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<NotificationChannel>>;
      })
    );
  }
  /**
   * deleteNotificationChannel
   * @param channelId channelId
   * @return OK
   */
  deleteNotificationChannelUsingDELETE(channelId: string): __Observable<Array<NotificationChannel>> {
    return this.deleteNotificationChannelUsingDELETEResponse(channelId).pipe(
      __map(_r => _r.body as Array<NotificationChannel>)
    );
  }

  /**
   * getAllNotificationTypes
   * @return OK
   */
  getAllNotificationTypesUsingGETResponse(): __Observable<__StrictHttpResponse<Array<'APP_DEPLOYMENT' | 'QASUITE_SANITY' | 'DR_RESULT' | 'STACK_SIGNOFF' | 'ALERT'>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/notification/notificationTypes`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<'APP_DEPLOYMENT' | 'QASUITE_SANITY' | 'DR_RESULT' | 'STACK_SIGNOFF' | 'ALERT'>>;
      })
    );
  }
  /**
   * getAllNotificationTypes
   * @return OK
   */
  getAllNotificationTypesUsingGET(): __Observable<Array<'APP_DEPLOYMENT' | 'QASUITE_SANITY' | 'DR_RESULT' | 'STACK_SIGNOFF' | 'ALERT'>> {
    return this.getAllNotificationTypesUsingGETResponse().pipe(
      __map(_r => _r.body as Array<'APP_DEPLOYMENT' | 'QASUITE_SANITY' | 'DR_RESULT' | 'STACK_SIGNOFF' | 'ALERT'>)
    );
  }

  /**
   * getAllSubscriptions
   * @return OK
   */
  getAllSubscriptionsUsingGETResponse(): __Observable<__StrictHttpResponse<Array<Subscription>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/cc-ui/v1/notification/subscriptions`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Subscription>>;
      })
    );
  }
  /**
   * getAllSubscriptions
   * @return OK
   */
  getAllSubscriptionsUsingGET(): __Observable<Array<Subscription>> {
    return this.getAllSubscriptionsUsingGETResponse().pipe(
      __map(_r => _r.body as Array<Subscription>)
    );
  }

  /**
   * createSubscription
   * @param subscription subscription
   * @return OK
   */
  createSubscriptionUsingPOSTResponse(subscription: Subscription): __Observable<__StrictHttpResponse<Array<Subscription>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = subscription;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/cc-ui/v1/notification/subscriptions`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Subscription>>;
      })
    );
  }
  /**
   * createSubscription
   * @param subscription subscription
   * @return OK
   */
  createSubscriptionUsingPOST(subscription: Subscription): __Observable<Array<Subscription>> {
    return this.createSubscriptionUsingPOSTResponse(subscription).pipe(
      __map(_r => _r.body as Array<Subscription>)
    );
  }

  /**
   * editSubscription
   * @param params The `UiNotificationControllerService.EditSubscriptionUsingPUTParams` containing the following parameters:
   *
   * - `subscriptionId`: subscriptionId
   *
   * - `subscription`: subscription
   *
   * @return OK
   */
  editSubscriptionUsingPUTResponse(params: UiNotificationControllerService.EditSubscriptionUsingPUTParams): __Observable<__StrictHttpResponse<Array<Subscription>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    __body = params.subscription;
    let req = new HttpRequest<any>(
      'PUT',
      this.rootUrl + `/cc-ui/v1/notification/subscriptions/${encodeURIComponent(params.subscriptionId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Subscription>>;
      })
    );
  }
  /**
   * editSubscription
   * @param params The `UiNotificationControllerService.EditSubscriptionUsingPUTParams` containing the following parameters:
   *
   * - `subscriptionId`: subscriptionId
   *
   * - `subscription`: subscription
   *
   * @return OK
   */
  editSubscriptionUsingPUT(params: UiNotificationControllerService.EditSubscriptionUsingPUTParams): __Observable<Array<Subscription>> {
    return this.editSubscriptionUsingPUTResponse(params).pipe(
      __map(_r => _r.body as Array<Subscription>)
    );
  }

  /**
   * deleteSubscription
   * @param subscriptionId subscriptionId
   * @return OK
   */
  deleteSubscriptionUsingDELETEResponse(subscriptionId: string): __Observable<__StrictHttpResponse<Array<Subscription>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'DELETE',
      this.rootUrl + `/cc-ui/v1/notification/subscriptions/${encodeURIComponent(subscriptionId)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<Subscription>>;
      })
    );
  }
  /**
   * deleteSubscription
   * @param subscriptionId subscriptionId
   * @return OK
   */
  deleteSubscriptionUsingDELETE(subscriptionId: string): __Observable<Array<Subscription>> {
    return this.deleteSubscriptionUsingDELETEResponse(subscriptionId).pipe(
      __map(_r => _r.body as Array<Subscription>)
    );
  }
}

module UiNotificationControllerService {

  /**
   * Parameters for editNotificationChannelUsingPUT
   */
  export interface EditNotificationChannelUsingPUTParams {

    /**
     * nc
     */
    nc: NotificationChannel;

    /**
     * channelId
     */
    channelId: string;
  }

  /**
   * Parameters for editSubscriptionUsingPUT
   */
  export interface EditSubscriptionUsingPUTParams {

    /**
     * subscriptionId
     */
    subscriptionId: string;

    /**
     * subscription
     */
    subscription: Subscription;
  }
}

export { UiNotificationControllerService }
