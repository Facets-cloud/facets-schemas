import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Application, Environment, EnvironmentMetaData } from '../../../api/models';
import { ApplicationControllerService } from '../../../api/services';
import { ButtonDownloadComponent } from './button.download.component';

@Component({
  selector: 'app-dumps',
  templateUrl: './app-dumps.component.html',
  styleUrls: ['./app-dumps.component.scss']
})
export class AppDumpsComponent implements OnInit, OnChanges {
  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  @Input() application: Application;

  @Input() rowData: Object;

  environments: Array<EnvironmentMetaData> = [];

  environment: string;

  date: Date = new Date();

  dumpFiles: Array<Object> = [];

  showDumpFiles: boolean = false;

  settings = {
    columns: {
      hour: {
        title: 'Hour',
      },
      name: {
        title: 'Name',
      },
      custom: {
        type: 'custom',
        renderComponent: ButtonDownloadComponent,
        title: 'Download',
      },
    },
    actions: false,
    hideSubHeader: true,
  };

  constructor(private applicationControllerService: ApplicationControllerService) { }

  ngOnInit() {
    this.loadEnvironments();
  }

  loadEnvironments() {
    if (!this.application.id) {
      return;
    }

    this.applicationControllerService
      .getEnvironmentMetaDataUsingGET(this.application.applicationFamily)
      .subscribe(meta => {
        this.environments = meta;
      });
  }

  getFileHour(fileString: string) {
    const dateParts = fileString.split('/');
    const dateTime = dateParts[dateParts.length - 2];
    const dateTimeParts = dateTime.split('H');

    return dateTimeParts[dateTimeParts.length - 1];
  }

  fetchDumpFileList(appFamily: any, applicationId: string, env: string, date: string) {
    this.applicationControllerService.getDumpFileListUsingGET({
      environment: env,
      applicationId: applicationId,
      applicationFamily: appFamily,
      date: date,
    }).subscribe(files => {
      const dumps = [];
      Object.keys(files).forEach(file => {
        const fileParts = file.split('/');
        dumps.push({
          hour: this.getFileHour(file),
          name: fileParts[fileParts.length - 1],
          path: files[file],
          application: this.application,
          environment: this.environment,
          date: date,
        });
      });
      this.dumpFiles = dumps;
      console.log(this.dumpFiles);
      this.showDumpFiles = true;
    });
  }

  listDumpFiles() {
    const app = this.application;
    const dateString = this.date.toISOString().split('T')[0];
    this.fetchDumpFileList(app.applicationFamily, app.id, this.environment, dateString);
  }

  handleDateChange(event) {
    this.date = event;
  }
}
