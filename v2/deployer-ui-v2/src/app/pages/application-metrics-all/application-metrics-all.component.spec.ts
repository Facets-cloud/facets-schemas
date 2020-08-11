import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationMetricsAllComponent } from './application-metrics-all.component';

describe('ApplicationMetricsAllComponent', () => {
  let component: ApplicationMetricsAllComponent;
  let fixture: ComponentFixture<ApplicationMetricsAllComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicationMetricsAllComponent]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationMetricsAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
