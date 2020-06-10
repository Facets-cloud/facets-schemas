import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SimpleOauth2User} from '../../cc-api/models/simple-oauth-2user';
import {ApplicationControllerService} from '../../cc-api/services/application-controller.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isClusterContext = true;
  user: SimpleOauth2User;

  constructor(private route: ActivatedRoute, private applicationControllerService: ApplicationControllerService) {
  }

  ngOnInit(): void {
    this.applicationControllerService.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
      },
    );
    this.route.params.subscribe(p => {
      if (!p.clusterId) {
        console.log(p);
        this.isClusterContext = false;
      }
    });
  }

}
