import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterAlertsComponent } from './cluster-alerts.component';

describe('ClusterAlertsComponent', () => {
  let component: ClusterAlertsComponent;
  let fixture: ComponentFixture<ClusterAlertsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterAlertsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterAlertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
