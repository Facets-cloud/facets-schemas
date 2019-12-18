import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExcecutePodActionDialogComponent } from './excecute-pod-action-dialog.component';

describe('ExcecutePodActionDialogComponent', () => {
  let component: ExcecutePodActionDialogComponent;
  let fixture: ComponentFixture<ExcecutePodActionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExcecutePodActionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExcecutePodActionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
