import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildDetailsPageComponent } from './build-details-page.component';

describe('BuildDetailsPageComponent', () => {
  let component: BuildDetailsPageComponent;
  let fixture: ComponentFixture<BuildDetailsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildDetailsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
