package br.com.cecafes.util;

import javax.servlet.http.Cookie;

public class CookieUtil {

    public static boolean verificaCookies(Cookie[] cookies, String cookieName){
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)){
                return true;
            }
        }
        return false;
    }

    public static Cookie procurarCookie(Cookie[] cookies, String cookieName){
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)){
                return cookie;
            }
        }

        return null;
    }

    public static Cookie resgatarCookies(Cookie[] cookies, String cookieName){
        for (Cookie cookie : cookies) {
            if(cookie.getName().equalsIgnoreCase(cookieName)){
                return cookie;
            }
        }
        return null;
    }

}
