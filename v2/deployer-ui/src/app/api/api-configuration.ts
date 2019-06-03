/* tslint:disable */
import { Injectable } from '@angular/core';

/**
 * Global configuration for Api services
 */
@Injectable({
  providedIn: 'root',
})
export class ApiConfiguration {
  rootUrl: string = '//deployer.capillary.in';
}

export interface ApiConfigurationInterface {
  rootUrl?: string;
}
