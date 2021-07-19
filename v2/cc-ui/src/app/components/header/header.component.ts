import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SimpleOauth2User} from '../../cc-api/models/simple-oauth-2user';
import {ApplicationControllerService} from '../../cc-api/services/application-controller.service';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {AbstractCluster} from '../../cc-api/models/abstract-cluster';
import {CookieService} from "ngx-cookie-service";
import {NbMenuService, NbSidebarService} from "@nebular/theme";
import {filter} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isClusterContext = true;
  user: SimpleOauth2User;
  currentNav = '';
  private BASE_URL = "/capc/";
  currentClusterURL;
  clusters: AbstractCluster[];
  isDemo: string = '';
  userItems: any = [
    {title: 'Profile'},
    {title: 'Log out'},
  ];
  settingsItems: any = [
    {title: 'Users', url: '/capc/users'},
    {title: 'Notifications', url: '/capc/notification-center'},
    {title: 'Artifactories', url: '/capc/artifactories'},
    {title: 'OAuth Integrations', url: '/capc/users/oauthClients'},
  ];
  stackClusters = {};
  newNav = [];
  private stackName: string;
  userName: any;
  logo: any;


  constructor(private route: ActivatedRoute, private applicationControllerService: ApplicationControllerService,
              private router: Router, private uiStackControllerService: UiStackControllerService,
              private cookieService: CookieService, private nbMenuService: NbMenuService, protected http: HttpClient,
              public sidebarService: NbSidebarService) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };
  }

  initNav(): void {
    this.newNav = [
      {
        groupName: "Stack",
        pages: [
          {
            title: "All Stacks",
            url: this.BASE_URL + "home",
          },
          {
            title: "Create Stack",
            url: this.BASE_URL + "createStack"
          }
        ]
      },
      {
        groupName: "Cluster Pages",
        pages: [
          {
            title: "Overview",
            url: this.BASE_URL + this.currentClusterURL + "/overview",
          },
          {
            title: "Overrides",
            url: this.BASE_URL + this.currentClusterURL + "/overrides"
          },
          {
            title: "Releases",
            url: this.BASE_URL + this.currentClusterURL + "/releases"
          },
          {
            title: "Database Snapshots",
            url: this.BASE_URL + this.currentClusterURL + "/disaster-recovery"
          },
          {
            title: "Alerts",
            url: this.BASE_URL + this.currentClusterURL + "/alerts"
          },
          {
            title: "Discover Resources",
            url: this.BASE_URL + this.currentClusterURL + "/resource-details"
          },
          {
            title: "Grafana",
            url: this.BASE_URL + this.currentClusterURL + "/tools"
          }

        ]
      }
    ]
  }

  ngOnInit(): void {

    this.http.get("public/v1/logo", {responseType: 'text'}).subscribe(
      x => {
        this.logo = x
      }
    )
    this.route.params.subscribe(p => {
      this.stackName = p.stackName;
      if (!p.clusterId) {
        console.log(p);
        this.isClusterContext = false;
      } else {
        this.currentClusterURL = p.stackName + "/cluster/" + p.clusterId;
      }
    });
    this.isDemo = this.cookieService.get("isDemo");
    this.currentNav = window.location.pathname
    this.applicationControllerService.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
        this.userName = x?.attributes["name"] || x?.name;
      }
    );
    this.uiStackControllerService.getStacksUsingGET1().subscribe(
      s => {
        s.forEach(stack => {
          this.uiStackControllerService.getClustersUsingGET1(stack.name).subscribe(
            c => {
              this.stackClusters[stack.name] = c;
              if (!this.currentClusterURL) {
                this.currentClusterURL = c[0].stackName + "/cluster/" + c[0].id
              }
              this.initNav()
            }
          );
        })
      }
    );
    this.addCallbacksToUserMenu()
  }

  addCallbacksToUserMenu(): void {
    this.nbMenuService.onItemClick()
      .pipe(
        filter(({tag}) => tag === 'user-menu'),
        filter(({item: {title}}) => title == 'Log out'),
      )
      .subscribe(title => {
        this.http.get(`/perform_logout`).subscribe(
          (x: SimpleOauth2User) => {
          },
          (error) => {
            window.location.reload()
          }
        );
      });
  }


  navToPage(): void {
    let navParts = this.currentNav.split('/');
    const url = this.BASE_URL + this.currentClusterURL + '/' + navParts[navParts.length - 1];

    this.router.navigate([url]);
  }

  navToPageLeft($event: any) {
    this.router.navigate([$event]);
  }
}
