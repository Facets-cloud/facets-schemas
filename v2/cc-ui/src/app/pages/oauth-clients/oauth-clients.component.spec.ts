import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OauthClientsComponent } from './oauth-clients.component';

describe('OauthClientsComponent', () => {
  let component: OauthClientsComponent;
  let fixture: ComponentFixture<OauthClientsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OauthClientsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OauthClientsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
