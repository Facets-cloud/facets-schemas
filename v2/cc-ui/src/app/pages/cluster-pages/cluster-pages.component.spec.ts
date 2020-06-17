import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterPagesComponent } from './cluster-pages.component';

describe('ClusterPagesComponent', () => {
  let component: ClusterPagesComponent;
  let fixture: ComponentFixture<ClusterPagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterPagesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterPagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
