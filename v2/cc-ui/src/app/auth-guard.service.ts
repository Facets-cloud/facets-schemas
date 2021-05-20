import {Inject, Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {NbAuthService} from '@nebular/auth';
import {catchError, map, tap} from 'rxjs/internal/operators';
import {ApplicationControllerService} from './cc-api/services/application-controller.service';
import {of} from 'rxjs/index';
import {DOCUMENT} from "@angular/common";

@Injectable()
export class AuthGuard implements CanActivate {

  private roles: string[] = [];

  constructor(private applicationControllerService: ApplicationControllerService,
              private router: Router, private authService: NbAuthService, @Inject(DOCUMENT) private document: any) {
  }

  canActivate() {
    return this.applicationControllerService.meUsingGET().pipe(
      map(res => {
        res.authorities.forEach(a => this.roles.push(a.authority.toLowerCase()))
        return true
      }),
      catchError(err => of(false)),
      tap(authenticated => {
        if (!authenticated) {
          const curr = this.document.location.href;
          this.document.location.href = '/capc/login';
        }
      })
    );
  }

  checkRole(roleName): boolean {
    return this.roles.indexOf("role_" + roleName.toLowerCase()) != -1
  }
}
