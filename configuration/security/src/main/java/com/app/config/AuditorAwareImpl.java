package com.app.config;

import com.app.error.exception.UnauthorizedException;
import com.app.model.CustomUser;
import com.app.util.SecurityMemberUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 시큐리티 기반으로 jpa audit 기능 활성화
 * createdBy, modifiedBy 자동 세팅
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            CustomUser customUser = SecurityMemberUtils.getCustomUser();
            return Optional.of(customUser.getUserId().toString());
        } catch (UnauthorizedException e) {
            return getRequestURIOrUnknown();
        } catch (Exception e) {
            return Optional.of("unknown");
        }
    }

    /**
     * 인증된 회원이 없을 경우 http request의 URI 기반으로 생성
     */
    private Optional<String> getRequestURIOrUnknown() {
        try {
            return Optional.of(httpServletRequest.getRequestURI());
        } catch (Exception e) {
            return Optional.of("unknown");
        }
    }

}