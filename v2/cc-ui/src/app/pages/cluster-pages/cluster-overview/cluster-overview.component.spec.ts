import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterOverviewComponent } from './cluster-overview.component';

describe('ClusterOverviewComponent', () => {
  let component: ClusterOverviewComponent;
  let fixture: ComponentFixture<ClusterOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
