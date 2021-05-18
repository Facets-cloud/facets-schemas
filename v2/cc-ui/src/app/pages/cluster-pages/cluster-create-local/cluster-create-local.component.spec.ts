import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterCreateLocalComponent } from './cluster-create-local.component';

describe('ClusterCreateLocalComponent', () => {
  let component: ClusterCreateLocalComponent;
  let fixture: ComponentFixture<ClusterCreateLocalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClusterCreateLocalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterCreateLocalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
