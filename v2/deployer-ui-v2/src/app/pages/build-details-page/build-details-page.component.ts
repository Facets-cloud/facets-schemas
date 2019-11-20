import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription, timer } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApplicationControllerService } from '../../api/services';
import { Build, LogEvent } from '../../api/models';

@Component({
  selector: 'build-details-page',
  templateUrl: './build-details-page.component.html',
  styleUrls: ['./build-details-page.component.scss']
})
export class BuildDetailsPageComponent implements OnInit {

  build: Build = {};

  constructor(private applicationControllerService: ApplicationControllerService,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.getBuildIdentifiers().subscribe(
      buildIdentifiers => {
        if (buildIdentifiers) {
          this.loadBuildDetails(buildIdentifiers);
        }
      },
    );
  }

  loadBuildDetails(buildIdentifiers: any) {
    this.applicationControllerService.getBuildUsingGET({
      buildId: buildIdentifiers['buildId'],
      applicationId: buildIdentifiers['applicationId'],
      applicationFamily: buildIdentifiers['appFamily'],
    }).subscribe(
      build => this.build = build,
    );
  }

  getBuildIdentifiers(): Observable<any> {
    return this.activatedRoute.paramMap.pipe(
      map(paramMap => {
        const buildIdentifiers = {};
        buildIdentifiers['applicationId'] = paramMap.get('applicationId');
        buildIdentifiers['appFamily'] = paramMap.get('appFamily');
        buildIdentifiers['buildId'] = paramMap.get('buildId');
        return buildIdentifiers;
      }),
    );
  }

  getArtifactLink() {
    const build = this.build;
    return `/api/${build.applicationFamily}/applications/${build.applicationId}/builds/${build.id}/downloadArtifacts`;
  }

}
