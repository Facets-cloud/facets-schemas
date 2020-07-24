import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterDisasterRecoveryComponent } from './cluster-disaster-recovery.component';

describe('ClusterDisasterRecoveryComponent', () => {
  let component: ClusterDisasterRecoveryComponent;
  let fixture: ComponentFixture<ClusterDisasterRecoveryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterDisasterRecoveryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterDisasterRecoveryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
