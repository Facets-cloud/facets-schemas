import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildLogsComponent } from './build-logs.component';

describe('BuildLogsComponent', () => {
  let component: BuildLogsComponent;
  let fixture: ComponentFixture<BuildLogsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildLogsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildLogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
