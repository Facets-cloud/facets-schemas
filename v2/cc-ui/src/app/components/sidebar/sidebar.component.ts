import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ApplicationControllerService} from "../../cc-api/services/application-controller.service";
import {UiStackControllerService} from "../../cc-api/services/ui-stack-controller.service";
import {CookieService} from "ngx-cookie-service";
import {NbMenuItem, NbMenuService} from "@nebular/theme";
import {HttpClient} from "@angular/common/http";
import {Stack} from "../../cc-api/models/stack";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  private stackName: any;
  private isClusterContext: boolean;
  private currentClusterURL: string;
  private stackClusters = [];
  private BASE_URL: string = "/capc";
  newNav: NbMenuItem[] = [];
  private stacks: Array<Stack>;


  constructor(private route: ActivatedRoute, private applicationControllerService: ApplicationControllerService,
              private router: Router, private uiStackControllerService: UiStackControllerService,
              private cookieService: CookieService, private nbMenuService: NbMenuService, protected http: HttpClient) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      this.stackName = p.stackName;
      if (!p.clusterId) {
        console.log(p);
        this.isClusterContext = false;
      } else {
        this.currentClusterURL = "/" + p.stackName + "/cluster/" + p.clusterId;
      }
    });
    this.uiStackControllerService.getStacksUsingGET1().subscribe(
      s => {
        this.stacks = s;
        if (!this.currentClusterURL) {
          var stack = this.stacks[0]
          this.uiStackControllerService.getClustersUsingGET1(stack.name).subscribe(
            c => {
              this.stackClusters[stack.name] = c;
              this.currentClusterURL = "/" + c[0].stackName + "/cluster/" + c[0].id
              this.initNav()
            }
          );
        } else {
          this.initNav()
        }

      }
    );
  }

  initNav(): void {
    var stacksMenu = []
    this.stacks.forEach(stack => {
      stacksMenu.push({
        title: stack.name,
        link: this.BASE_URL + "/stack/" + stack.name,
        icon: 'cloud-download-outline'
      })
    });

    this.newNav = [
      {
        title: 'Home',
        icon: 'home-outline',
        home: true,
        link: this.BASE_URL + "/home",
        pathMatch: 'prefix'
      },
      {
        title: 'Stack Overview',
        icon: 'layers-outline',
        expanded: false,
        link: this.BASE_URL + "/home",
        children: stacksMenu
      },
      {
        title: 'Cluster Pages',
        icon: 'cloud-download-outline',
        expanded: true,
        children: [
          {
            title: 'Overview',
            icon: 'clipboard-outline',
            link: this.BASE_URL + this.currentClusterURL + "/overview",
            pathMatch: 'full'
          },
          {
            title: 'Secrets & Variables',
            icon: 'options-outline',
            link: this.BASE_URL + this.currentClusterURL + "/variables",
            pathMatch: 'full'
          },
          {
            title: 'Releases',
            icon: 'sync-outline',
            link: this.BASE_URL + this.currentClusterURL + "/releases",
            pathMatch: 'full'
          },
          {
            title: 'Tools',
            icon: 'browser-outline',
            link: this.BASE_URL + this.currentClusterURL + "/tools",
          },
          {
            title: 'Alerts',
            icon: 'alert-triangle-outline',
            link: this.BASE_URL + this.currentClusterURL + "/alerts",
          },
          {
            title: 'Overrides',
            icon: 'at-outline',
            link: this.BASE_URL + this.currentClusterURL + "/overrides",
          },
          {
            title: 'Backups',
            icon: 'camera-outline',
            link: this.BASE_URL + this.currentClusterURL + "/disaster-recovery",
          },
          {
            title: 'Provided Resources',
            icon: 'cube-outline',
            link: this.BASE_URL + this.currentClusterURL + "/provided",
          },
          {
            title: 'Information',
            icon: 'info-outline',
            link: this.BASE_URL + this.currentClusterURL + "/resource-details",
          }
        ]
      }

    ]
  }


}
