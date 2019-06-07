import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppMenuPage } from './app-menu.page';

describe('AppMenuPage', () => {
  let component: AppMenuPage;
  let fixture: ComponentFixture<AppMenuPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppMenuPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppMenuPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
