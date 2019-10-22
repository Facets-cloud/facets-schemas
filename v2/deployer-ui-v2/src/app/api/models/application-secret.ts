/* tslint:disable */
export interface ApplicationSecret {
  secretName?: string;
  secretStatus?: 'FULFILLED' | 'UNFULFILLED' | 'PENDING';
  secretValue?: string;
}
