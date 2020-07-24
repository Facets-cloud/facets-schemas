import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PinSnapshotDialogComponent } from './pin-snapshot-dialog.component';

describe('PinSnapshotDialogComponent', () => {
  let component: PinSnapshotDialogComponent;
  let fixture: ComponentFixture<PinSnapshotDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PinSnapshotDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PinSnapshotDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
