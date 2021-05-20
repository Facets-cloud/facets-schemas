import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterOverviewLocalComponent } from './cluster-overview-local.component';

describe('ClusterOverviewLocalComponent', () => {
  let component: ClusterOverviewLocalComponent;
  let fixture: ComponentFixture<ClusterOverviewLocalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClusterOverviewLocalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterOverviewLocalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
