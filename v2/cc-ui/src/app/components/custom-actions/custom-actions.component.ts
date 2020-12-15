import { Component, OnInit, Input } from '@angular/core';
import { Router} from '@angular/router';
import {ApplicationControllerService } from '../../cc-api/services/application-controller.service';
import {SimpleOauth2User} from '../../cc-api/models/simple-oauth-2user';

@Component({
  selector: 'app-custom-actions',
  templateUrl: './custom-actions.component.html',
  styleUrls: ['./custom-actions.component.scss']
})
export class CustomActionsComponent implements OnInit {

  @Input() rowData: any;

  constructor(private router: Router,
              private applicationController: ApplicationControllerService) {}

  isCCAdmin: any;
  user: SimpleOauth2User;

  ngOnInit(): void {
    this.applicationController.meUsingGET().subscribe(
      (x: SimpleOauth2User) => {
        this.user = x;
        this.isCCAdmin = (this.user.authorities.map(x => x.authority).includes('ROLE_ADMIN'))
        || (this.user.authorities.map(x => x.authority).includes('ROLE_CC-ADMIN'))
        console.log(this.isCCAdmin)
      }
    );
  }

  view() {
    const clusterId = this.rowData.id;
    console.log('Navigate to ' + clusterId);
    this.router.navigate(['/capc/', this.rowData.stackName, 'cluster', clusterId]);
  }

  edit() {
    if(this.isCCAdmin){
    const clusterId = this.rowData.id;
    console.log('Navigate to ' + clusterId);
    this.router.navigate(['/capc/', this.rowData.stackName, 'cluster', clusterId, 'edit']);
    }
  }

}
