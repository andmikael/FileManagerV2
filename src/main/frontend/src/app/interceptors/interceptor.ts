import { isPlatformBrowser } from '@angular/common';
import { HttpEvent, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { PLATFORM_ID, inject } from '@angular/core';
import { Observable } from 'rxjs';

// credentials interceptor that inserts cookies to a request if cookies are present in the browser
export function credentialsInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
): Observable<HttpEvent<unknown>> {
  const platformId = inject(PLATFORM_ID);
  const isBrowser = isPlatformBrowser(platformId);
  let modifiedReq: HttpRequest<unknown>;

  if (!isBrowser) {
    modifiedReq = req;
    return next(modifiedReq);
  } 

  modifiedReq = req.clone({
    withCredentials: true,
  });
  return next(modifiedReq);
}