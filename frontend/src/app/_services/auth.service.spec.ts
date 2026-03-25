import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('login should POST username and password to signin endpoint', () => {
    const payload = { accessToken: 'token' };

    service.login('hamza', 'secret').subscribe((response) => {
      expect(response).toEqual(payload);
    });

    const req = httpMock.expectOne((request) =>
      request.url.endsWith('/auth/api/signin'),
    );
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ username: 'hamza', password: 'secret' });
    req.flush(payload);
  });

  it('register should POST registration payload to signup endpoint', () => {
    const payload = { message: 'User registered successfully!' };

    service
      .register('Hamza Afif', 'hamza', 'hamza@example.com', 'secret123')
      .subscribe((response) => {
        expect(response).toEqual(payload);
      });

    const req = httpMock.expectOne((request) =>
      request.url.endsWith('/auth/api/signup'),
    );
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      fullName: 'Hamza Afif',
      username: 'hamza',
      email: 'hamza@example.com',
      password: 'secret123',
    });
    req.flush(payload);
  });
});
