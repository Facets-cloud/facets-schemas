import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StackCreateComponent } from './stack-create.component';

describe('StackCreateComponent', () => {
  let component: StackCreateComponent;
  let fixture: ComponentFixture<StackCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StackCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StackCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
