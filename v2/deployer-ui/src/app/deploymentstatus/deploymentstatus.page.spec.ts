import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeploymentstatusPage } from './deploymentstatus.page';

describe('DeploymentstatusPage', () => {
  let component: DeploymentstatusPage;
  let fixture: ComponentFixture<DeploymentstatusPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeploymentstatusPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeploymentstatusPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
