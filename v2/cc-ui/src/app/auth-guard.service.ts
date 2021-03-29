import {Inject, Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {NbAuthService} from '@nebular/auth';
import {catchError, map, tap} from 'rxjs/internal/operators';
import {ApplicationControllerService} from './cc-api/services/application-controller.service';
import {SimpleOauth2User} from './cc-api/models/simple-oauth-2user';
import {from, Observable, of} from 'rxjs/index';
import {DOCUMENT} from "@angular/common";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private applicationControllerService: ApplicationControllerService,
              private router: Router, private authService: NbAuthService, @Inject(DOCUMENT) private document: any) {
  }

  canActivate() {
    return this.applicationControllerService.meUsingGET().pipe(
      map(res => true),
      catchError(err => of(false)),
      tap(authenticated => {
        if (!authenticated) {
          const curr = this.document.location.href;
          this.document.location.href = '/capc/login';
        }
      })
    );
  }
}
