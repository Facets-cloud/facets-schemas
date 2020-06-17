import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LayoutOneColumnComponent } from './layout-one-column.component';

describe('LayoutOneColumnComponent', () => {
  let component: LayoutOneColumnComponent;
  let fixture: ComponentFixture<LayoutOneColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LayoutOneColumnComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LayoutOneColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
