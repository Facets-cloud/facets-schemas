import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationMetricsComponent } from './application-metrics.component';

describe('ApplicationMetricsComponent', () => {
  let component: ApplicationMetricsComponent;
  let fixture: ComponentFixture<ApplicationMetricsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApplicationMetricsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationMetricsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
