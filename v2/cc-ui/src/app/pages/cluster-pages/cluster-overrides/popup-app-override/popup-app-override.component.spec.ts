import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PopupAppOverrideComponent } from './popup-app-override.component';

describe('PopupAppOverrideComponent', () => {
  let component: PopupAppOverrideComponent;
  let fixture: ComponentFixture<PopupAppOverrideComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PopupAppOverrideComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PopupAppOverrideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
