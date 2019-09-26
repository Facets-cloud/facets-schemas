import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberComponentDynamicComponent } from './number-component-dynamic.component';

describe('NumberComponentDynamicComponent', () => {
  let component: NumberComponentDynamicComponent;
  let fixture: ComponentFixture<NumberComponentDynamicComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NumberComponentDynamicComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberComponentDynamicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
