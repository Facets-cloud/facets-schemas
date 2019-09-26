import { Component, OnInit, Input } from '@angular/core';
import { Build } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';

@Component({
  selector: 'build-info',
  templateUrl: './build-info.component.html',
  styleUrls: ['./build-info.component.scss']
})
export class BuildInfoComponent implements OnInit {

  @Input() build: Build;

  constructor(private applicationControllerService: ApplicationControllerService) { }

  ngOnInit() {
    this.applicationControllerService.getBuildUsingGET({
      buildId: this.build.id,
      applicationFamily: this.build.applicationFamily,
      applicationId: this.build.applicationId,
    }).subscribe(build => this.build = build);
  }

}
