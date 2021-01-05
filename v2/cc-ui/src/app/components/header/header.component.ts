import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SimpleOauth2User} from '../../cc-api/models/simple-oauth-2user';
import {ApplicationControllerService} from '../../cc-api/services/application-controller.service';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {AbstractCluster} from '../../cc-api/models/abstract-cluster';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isClusterContext = true;
  user: SimpleOauth2User;
  currentNav = '';
  navs = [
    'overview', 'overrides', 'releases', 'disaster-recovery', 'alerts'
  ];
  clusters: AbstractCluster[];
  currentCluster = '';

  constructor(private route: ActivatedRoute, private applicationControllerService: ApplicationControllerService,
              private router: Router, private uiStackControllerService: UiStackControllerService) {
  }

  ngOnInit(): void {
    this.route.url.subscribe(
      s => this.currentNav = s[0].path
    );
    this.applicationControllerService.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
      },
    );
    this.route.params.subscribe(p => {
      if (!p.clusterId) {
        console.log(p);
        this.isClusterContext = false;
      } else {
        this.currentCluster = p.clusterId;
        if (p.stackName) {
          this.uiStackControllerService.getClustersUsingGET1(p.stackName).subscribe(
            c => {
              this.clusters = c;
            }
          );
        }
      }
    });
  }

  navToPage(): void {
    const url = '../' + this.currentNav;
    this.router.navigate([url], {relativeTo: this.route});
  }

  navToCluster(): void {
    const url = '../../' + this.currentCluster + '/' + this.currentNav;
    this.router.navigate([url], {relativeTo: this.route});
  }
}
