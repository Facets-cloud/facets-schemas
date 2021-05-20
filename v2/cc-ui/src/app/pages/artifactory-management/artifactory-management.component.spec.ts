import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtifactoryManagementComponent } from './artifactory-management.component';

describe('ArtifactoryManagementComponent', () => {
  let component: ArtifactoryManagementComponent;
  let fixture: ComponentFixture<ArtifactoryManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArtifactoryManagementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArtifactoryManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
