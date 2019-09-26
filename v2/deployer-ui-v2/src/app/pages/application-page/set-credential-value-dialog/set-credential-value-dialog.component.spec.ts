import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SetCredentialValueDialogComponent } from './set-credential-value-dialog.component';

describe('SetCredentialValueDialogComponent', () => {
  let component: SetCredentialValueDialogComponent;
  let fixture: ComponentFixture<SetCredentialValueDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SetCredentialValueDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SetCredentialValueDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
