import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationsMenuComponent } from './applications-menu.component';

describe('ApplicationsMenuComponent', () => {
  let component: ApplicationsMenuComponent;
  let fixture: ComponentFixture<ApplicationsMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApplicationsMenuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationsMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
