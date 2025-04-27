package com.app.util;

import com.app.error.ErrorType;
import com.app.error.exception.UnauthorizedException;
import com.app.model.CustomUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityMemberUtils {

    public static CustomUser getCustomUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) throw new UnauthorizedException(ErrorType.NOT_EXISTS_AUTH_MEMBER);
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        return (CustomUser) authToken.getPrincipal();
    }

}
