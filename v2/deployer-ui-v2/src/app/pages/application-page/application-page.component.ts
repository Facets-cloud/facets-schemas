import { Component, OnInit, TemplateRef } from '@angular/core';
import { ApplicationControllerService } from '../../api/services';
import { ActivatedRoute } from '@angular/router';
import { Ng2SmartTableTbodyComponent } from 'ng2-smart-table/lib/components/tbody/tbody.component';
import { Application, Build } from '../../api/models';
import { NbDialogService } from '@nebular/theme';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Component({
  selector: 'application-page',
  templateUrl: './application-page.component.html',
  styleUrls: ['./application-page.component.scss']
})
export class ApplicationPageComponent implements OnInit {

  application: Application = {};

  builds: Build[];

  constructor(private applicationControllerService: ApplicationControllerService,
    private activatedRoute: ActivatedRoute, private dialogService: NbDialogService) { }

  ngOnInit() {
    this.getAppIdentifiers().subscribe(
      appIdentifiers => {
        if (appIdentifiers) {
          this.loadApplication(appIdentifiers);
        }
      },
    );
  }

  loadApplication(appIdentifiers) {
    this.applicationControllerService.getApplicationUsingGET(
      {
        applicationFamily: appIdentifiers['appFamily'],
        applicationId: appIdentifiers['id'],
      },
    ).subscribe(
      app => this.application = app,
    );
  }

  getAppIdentifiers(): Observable<any> {
    return this.activatedRoute.paramMap.pipe(
      map(paramMap => {
        const appIdentifiers = {};
        appIdentifiers['id'] = paramMap.get('applicationId');
        appIdentifiers['appFamily'] = paramMap.get('appFamily');
        return appIdentifiers;
      }),
    );
  }

}
