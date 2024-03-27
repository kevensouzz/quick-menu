import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {

  interface ExtendedJwtPayload extends JwtPayload {
    email?: string;
    username?: string;
    role?: string;
  }

  const router = inject(Router)

  function setCookie(name: string, value: string, expireHours: number) {
    let d: Date = new Date();
    d.setTime(d.getTime() + expireHours * 60 * 60 * 1000);
    let expires: string = `expires=${d.toUTCString()}`;
    document.cookie = `${name}=${value}; ${expires}`;
  }

  function getCookie(name: string) {
    let cookies: Array<string> = document.cookie.split(';');

    for (let cookie of cookies) {
      let [cookieName, cookieValue] = cookie.trim().split('=');

      if (cookieName === name) {
        return decodeURIComponent(cookieValue);
      }
    }

    return null;
  }

  function deleteCookie(name: string) {
    setCookie(name, "", -1)
  }

  const token = getCookie("token")

  switch (router.url) {
    case "/admin":
      if (token) {
        try {
          let decodedToken = <ExtendedJwtPayload>jwtDecode(token)
          const isExpired = decodedToken && decodedToken.exp ? decodedToken.exp < Date.now() / 1000 : false

          if (isExpired || !decodedToken.iss || decodedToken.iss != "Quickmenu") {
            deleteCookie("token")
            router.navigateByUrl("generate/auth")
          } else if (decodedToken.role !== "ADMIN") {
            router.navigateByUrl("generate")
          }
        } catch (error) {
          deleteCookie("token")
          router.navigateByUrl("generate/auth")
        }
      } else {
        deleteCookie("token")
        router.navigateByUrl("generate/auth")
      }
      break;

    case "/generate":
      if (token) {
        try {
          let decodedToken = jwtDecode(token)
          const isExpired = decodedToken && decodedToken.exp ? decodedToken.exp < Date.now() / 1000 : false

          if (isExpired || !decodedToken.iss || decodedToken.iss != "Quickmenu") {
            deleteCookie("token")
            router.navigateByUrl("generate/auth")
          }
        } catch (error) {
          deleteCookie("token")
          router.navigateByUrl("generate/auth")
        }
      } else {
        deleteCookie("token")
        router.navigateByUrl("generate/auth")
      }
      break;

    case "/generate/auth":
      if (token) {
        try {
          let decodedToken = <ExtendedJwtPayload>jwtDecode(token)
          const isExpired = decodedToken && decodedToken.exp ? decodedToken.exp < Date.now() / 1000 : false

          if (isExpired === false && decodedToken.iss === "Quickmenu") {
            if (decodedToken.role === "ADMIN") {
              router.navigateByUrl("admin")
            } else {
              router.navigateByUrl("generate")
            }
          }
        } catch (error) {
          console.log(error);
        }
      }
      break;
  }

  return next(req);
}
