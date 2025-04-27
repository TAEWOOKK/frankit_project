package com.app.model;

import com.app.domain.member.constant.Role;
import com.app.error.ErrorType;
import com.app.error.exception.ForbiddenException;
import com.app.error.exception.UnauthorizedException;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {

    private final Long userId;

    public CustomUser(Long userId, String userName, String password, Collection<GrantedAuthority> authorities) {
        super(userName, password, authorities);
        this.userId = userId;
    }

    public boolean hasAdminRole() {
        Collection<GrantedAuthority> authorities = getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (Role.ROLE_ADMIN.name().equals(authority.getAuthority()) || Role.ROLE_SUPER_ADMIN.name().equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSuperAdminRole() {
        Collection<GrantedAuthority> authorities = getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (Role.ROLE_SUPER_ADMIN.name().equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPartnerRole() {
        Collection<GrantedAuthority> authorities = getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (Role.ROLE_PARTNER.name().equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public void validateSuperAdminRole() {
        Collection<GrantedAuthority> authorities = getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (Role.ROLE_SUPER_ADMIN.name().equals(authority.getAuthority())) {
                return;
            }
        }
        throw new ForbiddenException(ErrorType.NOT_SUPER_ADMIN_MEMBER);
    }

}