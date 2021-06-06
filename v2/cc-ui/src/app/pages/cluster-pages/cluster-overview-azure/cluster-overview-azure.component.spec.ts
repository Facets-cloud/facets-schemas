import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterOverviewAzureComponent } from './cluster-overview-azure.component';

describe('ClusterOverviewAzureComponent', () => {
  let component: ClusterOverviewAzureComponent;
  let fixture: ComponentFixture<ClusterOverviewAzureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterOverviewAzureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterOverviewAzureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
