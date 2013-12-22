package com.seguim.util;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;

/**
 * User: adria
 * Date: 16/06/13
 * Time: 16:52
 */
public class LoginUtil {
    public static UserDetails getPrincipal() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj instanceof UserDetails)
            return (UserDetails) obj;
        else
            return null;
    }

    public static void doLogin(String username, String password, HttpSession session) {
        // log user in automatically
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
        try {
            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
            if (ctx != null) {
                ProviderManager authenticationManager = (ProviderManager) ctx.getBean("authenticatorManager");
                SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));
            }
        } catch (NoSuchBeanDefinitionException n) {
            //ignore, should only happen when testing
        }

    }
}
