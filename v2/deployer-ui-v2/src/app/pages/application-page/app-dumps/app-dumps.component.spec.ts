import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppDumpsComponent } from './app-dumps.component';

describe('AppDumpsComponent', () => {
  let component: AppDumpsComponent;
  let fixture: ComponentFixture<AppDumpsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppDumpsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppDumpsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
