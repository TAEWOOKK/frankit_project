package com.app.handler;

import com.app.model.CustomUser;
import com.app.util.SecurityMemberUtils;

/**
 * 해당 자원에 접근할 수 있는지 권한 판단하도록 도와주는 추상 클래스
 */
public abstract class AdminAccessHandler {
    public final boolean check(Long id) {
        return hasSuperAdminRole() || isResourceOwner(id);
    }

    abstract protected boolean isResourceOwner(Long id);

    private boolean hasSuperAdminRole() {
        CustomUser customUser = SecurityMemberUtils.getCustomUser();
        return customUser.hasSuperAdminRole();
    }

}