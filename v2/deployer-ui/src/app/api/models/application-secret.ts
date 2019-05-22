/* tslint:disable */
export interface ApplicationSecret {
  description?: string;
  secretName?: string;
  secretStatus?: 'FULFILLED' | 'UNFULFILLED' | 'PENDING';
  secretValue?: string;
}
