import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestCredentialDialogComponent } from './request-credential-dialog.component';

describe('RequestCredentialDialogComponent', () => {
  let component: RequestCredentialDialogComponent;
  let fixture: ComponentFixture<RequestCredentialDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RequestCredentialDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestCredentialDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
