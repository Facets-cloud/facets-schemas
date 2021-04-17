import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ComponentUpgradeDialogComponent } from './componentupgradedialog.component';

describe('ComponentupgradedialogComponent', () => {
  let component: ComponentUpgradeDialogComponent;
  let fixture: ComponentFixture<ComponentUpgradeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ComponentUpgradeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ComponentUpgradeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
