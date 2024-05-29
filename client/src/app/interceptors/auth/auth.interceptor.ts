import { HttpClient, HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
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

  switch (router.url) {
    case "/admin":
      if (token) {
        try {
          let decodedToken = <ExtendedJwtPayload>jwtDecode(token)
          const isExpired = decodedToken && decodedToken.exp ? decodedToken.exp < Date.now() / 1000 : false

          if (isExpired || !decodedToken.iss || decodedToken.iss != "Quickmenu") {
            cookie.delete("token")
            router.navigateByUrl("generate/auth")
          } else if (decodedToken.role !== "ADMIN") {
            router.navigateByUrl("generate")
          }
        } catch (error) {
          cookie.delete("token")
          router.navigateByUrl("generate/auth")
        }
      } else {
        cookie.delete("token")
        router.navigateByUrl("generate/auth")
      }
      break;

    case "/generate":
    case "/generate/account":
    case "/generate/update":
    case "/generate/new":
      if (token) {
        try {
          let decodedToken = jwtDecode(token)
          const isExpired = decodedToken && decodedToken.exp ? decodedToken.exp < Date.now() / 1000 : false

          if (isExpired || !decodedToken.iss || decodedToken.iss != "Quickmenu") {
            cookie.delete("token")
            router.navigateByUrl("generate/auth")
          }
        } catch (error) {
          cookie.delete("token")
          router.navigateByUrl("generate/auth")
        }
      } else {
        cookie.delete("token")
        router.navigateByUrl("generate/auth")
      }
      break;

    case "/generate/auth":
      if (token) {
        try {
          let decodedToken = <ExtendedJwtPayload>jwtDecode(token)
          const isExpired = decodedToken && decodedToken.exp ? decodedToken.exp < Date.now() / 1000 : false

          if (isExpired === false && decodedToken.iss === "Quickmenu") {
            router.navigateByUrl("generate")
          }
        } catch (error) {
          console.log(error);
        }
      }
      break;
  }

  return next(req);
}