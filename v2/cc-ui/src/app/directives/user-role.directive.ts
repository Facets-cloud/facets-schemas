import {Directive, Input, TemplateRef, ViewContainerRef} from '@angular/core';
import {AuthGuard} from "../auth-guard.service";

@Directive({
  selector: '[appUserRole]',
})
export class UserRoleDirective {

  constructor(private templateRef: TemplateRef<any>,
              private authGuard: AuthGuard,
              private viewContainer: ViewContainerRef) {
  }

  userRoles: string[];


  @Input()
  set appUserRole(roles: string[]) {
    if (!roles || !roles.length) {
      throw new Error('Roles value is empty or missed');
    }

    this.userRoles = roles;
  }

  ngOnInit() {
    let hasAccess = false;
    hasAccess = this.userRoles.some(r => this.authGuard.checkRole(r));
    if (hasAccess) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}

