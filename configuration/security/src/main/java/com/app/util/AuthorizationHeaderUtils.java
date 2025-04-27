package com.app.util;

import com.app.constant.GrantType;
import com.app.error.ErrorType;
import com.app.error.exception.UnauthorizedException;
import org.springframework.util.StringUtils;

public class AuthorizationHeaderUtils {

    public static void validateAuthorization(String authorizationHeader) {

        // 1. authorizationHeader 필수 체크
        if(!StringUtils.hasText(authorizationHeader)) {
            throw new UnauthorizedException(ErrorType.NOT_EXISTS_AUTHORIZATION);
        }

        // 2. authorizationHeader Bearer 체크
        String[] authorizations = authorizationHeader.split(" ");
        if(authorizations.length < 2 || (!GrantType.BEARER.getType().equals(authorizations[0]))) {
            throw new UnauthorizedException(ErrorType.NOT_VALID_BEARER_GRANT_TYPE);
        }
    }

}
