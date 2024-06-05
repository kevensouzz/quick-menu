import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';
import { CookiesService } from '../../services/cookies/cookies.service';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {

  interface ExtendedJwtPayload extends JwtPayload {
    id?: string;
    role?: string;
  }

  const router = inject(Router)
  const cookie = inject(CookiesService)

  const token = cookie.get("token")



  return next(req);
}
