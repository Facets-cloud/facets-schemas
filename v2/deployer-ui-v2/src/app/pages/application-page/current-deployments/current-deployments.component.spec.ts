import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentDeploymentsComponent } from './current-deployments.component';

describe('CurrentDeploymentsComponent', () => {
  let component: CurrentDeploymentsComponent;
  let fixture: ComponentFixture<CurrentDeploymentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrentDeploymentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentDeploymentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
