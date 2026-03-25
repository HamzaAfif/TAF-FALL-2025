import { HttpHandler, HttpRequest } from '@angular/common/http';
import { of } from 'rxjs';
import { TokenStorageService } from '../_services/token-storage.service';
import { AuthInterceptor } from './auth.interceptor';

describe('AuthInterceptor', () => {
  const createRequest = () =>
    new HttpRequest('GET', 'https://api.example.com/team3/api/test');

  it('should add x-access-token header when token exists', () => {
    const tokenService = {
      getToken: jasmine.createSpy('getToken').and.returnValue('abc123'),
    } as unknown as TokenStorageService;

    const interceptor = new AuthInterceptor(tokenService);
    const next: HttpHandler = {
      handle: jasmine.createSpy('handle').and.returnValue(of({} as any)),
    };

    interceptor.intercept(createRequest(), next).subscribe();

    expect(next.handle as jasmine.Spy).toHaveBeenCalled();
    const forwardedRequest = (next.handle as jasmine.Spy).calls.mostRecent()
      .args[0] as HttpRequest<unknown>;
    expect(forwardedRequest.headers.get('x-access-token')).toBe(
      'Bearer abc123',
    );
  });

  it('should forward original request when token is null', () => {
    const tokenService = {
      getToken: jasmine.createSpy('getToken').and.returnValue(null),
    } as unknown as TokenStorageService;

    const interceptor = new AuthInterceptor(tokenService);
    const originalRequest = createRequest();
    const next: HttpHandler = {
      handle: jasmine.createSpy('handle').and.returnValue(of({} as any)),
    };

    interceptor.intercept(originalRequest, next).subscribe();

    const forwardedRequest = (next.handle as jasmine.Spy).calls.mostRecent()
      .args[0] as HttpRequest<unknown>;
    expect(forwardedRequest).toBe(originalRequest);
    expect(forwardedRequest.headers.has('x-access-token')).toBeFalse();
  });
});
