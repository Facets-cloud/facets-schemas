import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterChooserComponent } from './cluster-chooser.component';

describe('ClusterChooserComponent', () => {
  let component: ClusterChooserComponent;
  let fixture: ComponentFixture<ClusterChooserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClusterChooserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterChooserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
