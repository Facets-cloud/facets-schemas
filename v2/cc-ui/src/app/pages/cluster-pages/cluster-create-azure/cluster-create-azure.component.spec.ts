import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AzureClusterCreateComponent } from './cluster-create-azure.component';

describe('ClusterCreateComponent', () => {
  let component: AzureClusterCreateComponent;
  let fixture: ComponentFixture<AzureClusterCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AzureClusterCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AzureClusterCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
