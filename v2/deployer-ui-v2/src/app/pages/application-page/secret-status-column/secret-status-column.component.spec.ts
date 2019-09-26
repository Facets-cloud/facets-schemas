import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SecretStatusColumnComponent } from './secret-status-column.component';

describe('SecretStatusColumnComponent', () => {
  let component: SecretStatusColumnComponent;
  let fixture: ComponentFixture<SecretStatusColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SecretStatusColumnComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecretStatusColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
