import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildListPage } from './build-list.page';

describe('BuildListPage', () => {
  let component: BuildListPage;
  let fixture: ComponentFixture<BuildListPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildListPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildListPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
