import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvidedResourcesComponent } from './provided-resources.component';

describe('ProvidedResourcesComponent', () => {
  let component: ProvidedResourcesComponent;
  let fixture: ComponentFixture<ProvidedResourcesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvidedResourcesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvidedResourcesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
