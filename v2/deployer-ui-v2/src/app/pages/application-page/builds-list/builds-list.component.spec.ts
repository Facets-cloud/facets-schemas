import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildsListComponent } from './builds-list.component';

describe('BuildsListComponent', () => {
  let component: BuildsListComponent;
  let fixture: ComponentFixture<BuildsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
