import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildPromoteComponent } from './build-promote.component';

describe('BuildPromoteComponent', () => {
  let component: BuildPromoteComponent;
  let fixture: ComponentFixture<BuildPromoteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildPromoteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildPromoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
