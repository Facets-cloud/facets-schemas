import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../../api/services';
import { NbMenuItem, NbMenuService } from '@nebular/theme';
import { Application, SimpleOauth2User } from '../../api/models';
import { UserService } from '../../@core/mock/users.service';
import { MessageBus } from '../../@core/message-bus';

@Component({
  selector: 'applications-menu',
  templateUrl: './applications-menu.component.html',
  styleUrls: ['./applications-menu.component.scss']
})
export class ApplicationsMenuComponent implements OnInit {

  menuItems: NbMenuItem[] = [];
  createAppMenuItems: NbMenuItem[] = [
    {
      title: 'Create Application',
      icon: 'plus-outline',
      link: 'applications/create',
    },
  ];

  userMgmtMenuItems: NbMenuItem[] = [
    {
      title: 'User Management',
      icon: 'people-outline',
      link: 'users',
    },
  ];

  applicationMetrics: NbMenuItem[] = [
    {
      title: 'Application Metrics',
      icon: 'thermometer-outline',
      link: 'family/metrics',
    },
  ];

  isModerator: boolean = false;
  isUserAdmin: boolean = false;

  constructor(private applicationControllerService: ApplicationControllerService,
    private nbMenuService: NbMenuService, private userService: UserService,
    private messageBus: MessageBus) { }

  ngOnInit() {
    this.userService.getUser().subscribe(user =>
        this.isModerator = (user.authorities.map(x => x.authority).includes('ROLE_MODERATOR'))
        || user.authorities.map(x => x.authority).includes('ROLE_ADMIN'));
    this.userService.getUser().subscribe(user =>
        this.isUserAdmin = (user.authorities.map(x => x.authority).includes('ROLE_ADMIN'))
        || user.authorities.map(x => x.authority).includes('ROLE_USER_ADMIN'));
    this.messageBus.application.subscribe(x => this.populateAppFamilies());
    this.populateAppFamilies();
  }
  populateAppFamilies() {
    this.menuItems = [];
    this.applicationControllerService.getApplicationFamiliesUsingGET().subscribe(
      appFamilies => {
        appFamilies.forEach(x => this.createAppFamilyMenuItem(x));
      },
    );
  }
  createAppFamilyMenuItem(appFamily: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'): void {
    const applications: Array<NbMenuItem> = [];
    this.applicationControllerService.getApplicationsUsingGET(appFamily).subscribe(
      apps => apps.map(x => {
        return {
          title: `${x.name}`,
          link: `applications/${appFamily}/${x.id}`,
          // pathMatch: 'prefix',
        };
      }).forEach(x => applications.push(x)),
    );
    const menuItem: NbMenuItem = {
      title: `${appFamily}`,
      icon: 'list',
      children: applications,
    };
    this.menuItems.push(menuItem);
  }

  search(searchString) {
    if (searchString.length === 0) {
      this.menuItems.forEach(
        x => {
          x.expanded = false;
          x.children.forEach(y => y.hidden = false);
        },
      );
    } else {
      this.menuItems.forEach(
        x => x.children.forEach(
          y => {
            if (y.title.includes(searchString)) {
              x.expanded = true;
              y.hidden = false;
            } else {
              y.hidden = true;
            }
          },
        ),
      );
    }
  }

}
