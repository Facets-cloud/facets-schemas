import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteOverrideDialogComponent } from './delete-override-dialog.component';

describe('DeleteOverrideDialogComponent', () => {
  let component: DeleteOverrideDialogComponent;
  let fixture: ComponentFixture<DeleteOverrideDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteOverrideDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteOverrideDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
