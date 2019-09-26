import { Observable } from 'rxjs';
import { SimpleOauth2User } from '../../api/models';

export interface User {
  name: string;
  picture: string;
}

export abstract class UserData {
  abstract getUser(): Observable<SimpleOauth2User>;
}
