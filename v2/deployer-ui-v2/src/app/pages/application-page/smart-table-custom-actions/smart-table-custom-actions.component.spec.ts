import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SmartTableCustomActionsComponent } from './smart-table-custom-actions.component';

describe('SmartTableCustomActionsComponent', () => {
  let component: SmartTableCustomActionsComponent;
  let fixture: ComponentFixture<SmartTableCustomActionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SmartTableCustomActionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SmartTableCustomActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
