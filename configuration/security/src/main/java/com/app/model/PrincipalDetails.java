package com.app.model;

import com.app.domain.member.entity.Member;
import com.app.domain.member.entity.MemberRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class PrincipalDetails implements OAuth2User, UserDetails {

    // 애플리케이션의 회원 엔티티를 나타냅니다.
    private final Member member;

    // 사용자의 역할 목록을 나타냅니다. 각 역할은 권한을 나타냄.
    private final List<MemberRole> memberRoles;

    // OAuth2 사용자 속성 맵 (Google, Naver, Kakao 등에서 제공하는 사용자 정보)
    private final Map<String, Object> attributesMap;

    // OAuth2 사용자 속성 맵에서 특정 키를 지정하여 사용자의 고유 ID를 가져오는 데 사용
    private final String attributeKey;

    /**
     * PrincipalDetails 생성자.
     * OAuth2 인증 및 UserDetails 인터페이스 구현을 통해 인증된 사용자 정보를 관리합니다.
     *
     * @param member       인증된 회원 정보를 나타내는 Member 객체
     * @param memberRoles  인증된 회원의 역할 목록
     * @param attributesMap OAuth2 제공자에서 받은 사용자 속성 맵
     * @param attributeKey OAuth2 사용자 속성 맵에서 사용자의 고유 ID를 가져오는 데 사용되는 키
     */
    public PrincipalDetails(Member member, List<MemberRole> memberRoles, Map<String, Object> attributesMap, String attributeKey) {
        this.member = member;
        this.memberRoles = memberRoles;
        this.attributesMap = attributesMap;
        this.attributeKey = attributeKey;
    }

    /**
     * OAuth2User 인터페이스의 getName() 메서드 구현.
     * OAuth2 사용자 속성 맵에서 지정된 키(attributeKey)에 해당하는 값을 반환.
     *
     * @return 사용자 고유 ID (OAuth2 제공자에서 제공하는 값)
     */
    @Override
    public String getName() {
        return attributesMap.get(attributeKey).toString();
    }

    /**
     * OAuth2User 인터페이스의 getAttributes() 메서드 구현.
     * OAuth2 제공자에서 제공한 사용자 속성 맵을 반환.
     *
     * @return 사용자 속성 맵
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributesMap;
    }

    /**
     * UserDetails 인터페이스의 getAuthorities() 메서드 구현.
     * 사용자의 역할 목록을 Spring Security에서 사용하는 GrantedAuthority로 변환하여 반환.
     *
     * @return 사용자 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return memberRoles.stream()
                .map(memberRole -> new SimpleGrantedAuthority(memberRole.getRole().name()))
                .collect(Collectors.toList());
    }

    /**
     * UserDetails 인터페이스의 getPassword() 메서드 구현.
     * 사용자의 비밀번호를 반환.
     *
     * @return 사용자 비밀번호
     */
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /**
     * UserDetails 인터페이스의 getUsername() 메서드 구현.
     * 사용자의 이름을 반환.
     *
     * @return 사용자 이름
     */
    @Override
    public String getUsername() {
        return member.getName();
    }

    /**
     * UserDetails 인터페이스의 isAccountNonExpired() 메서드 구현.
     * 계정의 만료 여부를 반환 (true이면 계정이 만료되지 않음을 의미).
     *
     * @return true (계정이 만료되지 않음을 의미)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * UserDetails 인터페이스의 isAccountNonLocked() 메서드 구현.
     * 계정의 잠김 여부를 반환 (true이면 계정이 잠기지 않음을 의미).
     *
     * @return true (계정이 잠기지 않음을 의미)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * UserDetails 인터페이스의 isCredentialsNonExpired() 메서드 구현.
     * 인증 정보의 만료 여부를 반환 (true이면 인증 정보가 만료되지 않음을 의미).
     *
     * @return true (인증 정보가 만료되지 않음을 의미)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * UserDetails 인터페이스의 isEnabled() 메서드 구현.
     * 계정의 활성화 여부를 반환 (true이면 계정이 활성화되어 있음을 의미).
     *
     * @return true (계정이 활성화되어 있음을 의미)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
