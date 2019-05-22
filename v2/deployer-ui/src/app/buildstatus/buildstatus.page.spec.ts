import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildstatusPage } from './buildstatus.page';

describe('BuildstatusPage', () => {
  let component: BuildstatusPage;
  let fixture: ComponentFixture<BuildstatusPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildstatusPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildstatusPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
