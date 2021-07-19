import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LayoutOneColumnSidebarComponent } from './layout-one-column-sidebar.component';

describe('LayoutOneColumnSidebarComponent', () => {
  let component: LayoutOneColumnSidebarComponent;
  let fixture: ComponentFixture<LayoutOneColumnSidebarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LayoutOneColumnSidebarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LayoutOneColumnSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
