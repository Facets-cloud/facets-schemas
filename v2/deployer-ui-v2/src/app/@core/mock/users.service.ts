import { of as observableOf,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { UserData, User } from '../data/users';
import { ApplicationControllerService } from '../../api/services';
import { SimpleOauth2User } from '../../api/models';

@Injectable()
export class UserService extends UserData {

  constructor(private applicationControllerService: ApplicationControllerService) {
    super();
  }

  getUser(): Observable<SimpleOauth2User> {
    return this.applicationControllerService.meUsingGET();
  }
}
