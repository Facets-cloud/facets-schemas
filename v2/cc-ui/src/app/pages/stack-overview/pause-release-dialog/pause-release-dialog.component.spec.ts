import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PauseReleaseDialogComponent } from './pause-release-dialog.component';

describe('PauseReleaseDialogComponent', () => {
  let component: PauseReleaseDialogComponent;
  let fixture: ComponentFixture<PauseReleaseDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PauseReleaseDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PauseReleaseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
