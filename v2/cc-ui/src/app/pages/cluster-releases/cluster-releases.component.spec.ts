import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterReleasesComponent } from './cluster-releases.component';

describe('ClusterReleasesComponent', () => {
  let component: ClusterReleasesComponent;
  let fixture: ComponentFixture<ClusterReleasesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterReleasesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterReleasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
