import { Component, OnInit, Input } from '@angular/core';
import { TestBuildDetails } from '../../../api/models';
import { Build } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';
import { Condition } from '../../../api/models';

@Component({
  selector: 'test-build-details',
  templateUrl: './test-build-details.component.html',
  styleUrls: ['./test-build-details.component.scss']
})
export class TestBuildDetailsComponent implements OnInit {

  @Input() build: Build;
  testBuildDetails: TestBuildDetails;

  constructor(private applicationControllerService: ApplicationControllerService) { }

  ngOnInit() {
    this.applicationControllerService.getTestBuildDetailsUsingGETResponse({
      buildId: this.build.id,
      applicationFamily: this.build.applicationFamily,
      applicationId: this.build.applicationId,
    }).subscribe(x => {
    this.testBuildDetails = x.body
    });
  }

}
