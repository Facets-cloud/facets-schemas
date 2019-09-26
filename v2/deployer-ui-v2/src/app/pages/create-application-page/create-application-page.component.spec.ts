import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateApplicationPageComponent } from './create-application-page.component';

describe('CreateApplicationPageComponent', () => {
  let component: CreateApplicationPageComponent;
  let fixture: ComponentFixture<CreateApplicationPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateApplicationPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateApplicationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
