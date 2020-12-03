import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourceTypeSelectorComponent } from './resource-type-selector.component';

describe('ResourceTypeSelectorComponent', () => {
  let component: ResourceTypeSelectorComponent;
  let fixture: ComponentFixture<ResourceTypeSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResourceTypeSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceTypeSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
