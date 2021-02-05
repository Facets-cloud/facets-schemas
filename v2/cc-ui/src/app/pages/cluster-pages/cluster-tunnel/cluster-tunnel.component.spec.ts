import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterTunnelComponent } from './cluster-tunnel.component';

describe('ClusterTunnelComponent', () => {
  let component: ClusterTunnelComponent;
  let fixture: ComponentFixture<ClusterTunnelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterTunnelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterTunnelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
