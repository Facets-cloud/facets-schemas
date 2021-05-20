import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterOverviewAssemblerComponent } from './cluster-overview-assembler.component';

describe('ClusterOverviewAssemblerComponent', () => {
  let component: ClusterOverviewAssemblerComponent;
  let fixture: ComponentFixture<ClusterOverviewAssemblerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClusterOverviewAssemblerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterOverviewAssemblerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
