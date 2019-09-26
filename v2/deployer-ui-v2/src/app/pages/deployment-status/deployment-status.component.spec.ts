import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeploymentStatusComponent } from './deployment-status.component';

describe('DeploymentStatusComponent', () => {
  let component: DeploymentStatusComponent;
  let fixture: ComponentFixture<DeploymentStatusComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeploymentStatusComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeploymentStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
