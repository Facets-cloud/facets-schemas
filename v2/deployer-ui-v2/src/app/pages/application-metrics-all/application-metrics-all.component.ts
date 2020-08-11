import {Component, OnChanges, OnInit} from '@angular/core';
import {ApplicationMetricsWrapper} from '../../api/models';
import {ApplicationControllerService} from '../../api/services';
import {ActivatedRoute} from '@angular/router';

class ApplicationMetricsAllUiWrapper {
  id ?: string;
  name?: string;
  buildFailures ?: string;
  unitTests?: string;
  unitTestCoverage ?: string;
  codeSmells ?: string;
  constructor(private apiObj: ApplicationMetricsWrapper) {
    this.name = apiObj.application.name;
    this.id = apiObj.application.id;
    this.buildFailures = this.getUiString(apiObj.recentMetrics.buildFailures, apiObj.lastWeekMetrics.buildFailures, false);
    this.unitTests = this.getUiString(apiObj.recentMetrics.unitTests, apiObj.lastWeekMetrics.unitTests, true);
    this.unitTestCoverage = this.getUiString(apiObj.recentMetrics.unitTestCoverage, apiObj.lastWeekMetrics.unitTestCoverage, true);
    this.codeSmells = this.getUiString(apiObj.recentMetrics.criticalCodeSmells, apiObj.lastWeekMetrics.criticalCodeSmells, false);
  }
  private getUiString(newValue: number, oldValue: number, greaterIsBetter: boolean = false) {
    newValue = newValue == null ? 0 : newValue;
    oldValue = oldValue == null ? 0 : oldValue;
    let text: string = '';
    let indicator: string = '';

    text = text.concat(newValue.toString());
    text = text.concat(' (');
    if(newValue == oldValue)
      indicator = "";
    else if( (newValue > oldValue) == greaterIsBetter )
      indicator = 'green';
    else if( (newValue < oldValue) == !greaterIsBetter )
      indicator = 'red';
    // text = text.concat(indicator);

    let diff: string ;
    if ( newValue > oldValue) {
      diff = '+'.concat( (newValue - oldValue).toString());
    } else if ( newValue < oldValue) {
      diff =  (newValue - oldValue).toString();
    } else {
      diff = '=';
    }
    //text = text.concat( '<span style="color:', indicator, '">', diff, ') </span>');
    text = text.concat(  diff, ')');
    return text;
  }
}
@Component({
  selector: 'application-metrics-all',
  templateUrl: './application-metrics-all.component.html',
  styleUrls: ['./application-metrics-all.component.scss']
})

export class ApplicationMetricsAllComponent implements OnInit, OnChanges {

  // @Input()
  applicationFamily?: 'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS';
  appFamilies: Array<"CRM" | "ECOMMERCE" | "INTEGRATIONS" | "OPS">;
  applicationMetricsWrappers: ApplicationMetricsWrapper [] = [];
  metricsList: ApplicationMetricsAllUiWrapper[] = [];

  ngOnChanges(changes: import("@angular/core").SimpleChanges): void {
    this.ngOnInit();
  }


  constructor(private applicationControllerService: ApplicationControllerService,
              private activatedRoute: ActivatedRoute) {
  }


  populateAppFamilies() {
    this.applicationControllerService.getApplicationFamiliesUsingGET().subscribe(
      appFamilies => {
        this.appFamilies = appFamilies;
      },
    );
  }

  ngOnInit() {
    // this.activatedRoute.paramMap.subscribe(
    //   paramMap => {
    //     this.applicationFamily = <'CRM' | 'ECOMMERCE' | 'INTEGRATIONS' | 'OPS'>paramMap.get('appFamily');
    //   });

    this.populateAppFamilies();

  }

  loadMetrics() {
    this.applicationControllerService.getAllApplicationMetricsUsingGETResponse(this.applicationFamily)
      .subscribe(resp => {

        this.applicationMetricsWrappers = resp.body;

        this.metricsList = [];
        this.applicationMetricsWrappers.forEach(t => {
            this.metricsList.push(new ApplicationMetricsAllUiWrapper(t));
        });

      });
  }

  settings = {
    columns: {
      name: {
        title: 'Application',
        filter: true,
      },
      buildFailures: {
        title: 'Build Failures',
        filter: false,
      },
      codeSmells: {
        title: 'Critical Code smells',
        filter: false,
      },
      unitTestCoverage: {
        title: 'UT coverage',
        filter: false,
      },
      unitTests: {
        title: 'Unit Tests',
        filter: false,
      },
    },
    noDataMessage: 'No data',
    pager: {
      display: true,
      perPage: 10,
    },
    actions: {
      position: 'right',
      delete: false,
      add: false,
      edit: false,
    },
  };
}




