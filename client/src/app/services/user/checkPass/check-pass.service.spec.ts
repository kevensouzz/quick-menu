import { TestBed } from '@angular/core/testing';

import { CheckPassService } from './check-pass.service';

describe('CheckPassService', () => {
  let service: CheckPassService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CheckPassService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
