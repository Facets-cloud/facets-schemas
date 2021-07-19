import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterVarsComponent } from './cluster-vars.component';

describe('ClusterVarsComponent', () => {
  let component: ClusterVarsComponent;
  let fixture: ComponentFixture<ClusterVarsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterVarsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterVarsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
