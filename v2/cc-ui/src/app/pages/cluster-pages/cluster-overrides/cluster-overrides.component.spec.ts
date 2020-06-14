import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterOverridesComponent } from './cluster-overrides.component';

describe('ClusterOverridesComponent', () => {
  let component: ClusterOverridesComponent;
  let fixture: ComponentFixture<ClusterOverridesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterOverridesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterOverridesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
