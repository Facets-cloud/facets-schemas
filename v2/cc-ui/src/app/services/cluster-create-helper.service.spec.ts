import { TestBed } from '@angular/core/testing';

import { ClusterCreateHelperService } from './cluster-create-helper.service';

describe('ClusterCreateHelperService', () => {
  let service: ClusterCreateHelperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClusterCreateHelperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
