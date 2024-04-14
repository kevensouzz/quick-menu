import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CookiesService {
  constructor() { }

  set(name: string, value: string, expireHours: number, path?: string) {
    let d: Date = new Date();
    d.setTime(d.getTime() + expireHours * 60 * 60 * 1000);
    let expires: string = `expires=${d.toUTCString()}`;
    let cpath:string = path ? `; path=${path}` : '';
    document.cookie = `${name}=${value}; ${expires}${cpath}`;
  }

  get(name: string) {
    let cookies: Array<string> = document.cookie.split(';');

    for (let cookie of cookies) {
      let [cookieName, cookieValue] = cookie.trim().split('=');

      if (cookieName === name) {
        return decodeURIComponent(cookieValue);
      }
    }

    return null;
  }

  delete(name: string) {
    this.set(name, "", -1, "/")
  }
}
