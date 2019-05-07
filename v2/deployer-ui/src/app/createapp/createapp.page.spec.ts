import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateappPage } from './createapp.page';

describe('CreateappPage', () => {
  let component: CreateappPage;
  let fixture: ComponentFixture<CreateappPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateappPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateappPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
