import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterResourceDetailsComponent } from './cluster-resource-details.component';

describe('ClusterResourceDetailsComponent', () => {
  let component: ClusterResourceDetailsComponent;
  let fixture: ComponentFixture<ClusterResourceDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterResourceDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterResourceDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
