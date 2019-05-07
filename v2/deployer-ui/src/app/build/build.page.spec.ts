import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildPage } from './build.page';

describe('BuildPage', () => {
  let component: BuildPage;
  let fixture: ComponentFixture<BuildPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
