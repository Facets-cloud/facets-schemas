import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutedActionsComponent } from './executed-actions.component';

describe('ExecutedActionsComponent', () => {
  let component: ExecutedActionsComponent;
  let fixture: ComponentFixture<ExecutedActionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExecutedActionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecutedActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
