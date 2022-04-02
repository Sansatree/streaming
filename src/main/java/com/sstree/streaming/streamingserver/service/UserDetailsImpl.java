//package com.sstree.streaming.streamingserver.service;
//
//import com.sstree.streaming.streamingserver.entity.Role;
//import com.sstree.streaming.streamingserver.entity.User;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Getter
//public class UserDetailsImpl implements UserDetails {
//
//    private User user;
//    private Role role;
//    public UserDetailsImpl(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> auth = new ArrayList<>();
//        for (String role : role.getRole().split(",")){
//            auth.add(new SimpleGrantedAuthority(role));
//        }
//        return auth;
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUserId();
//    }
//
//    /**
//     * 계정의 만료 여부
//     * @return true = 만료 안됨 , false =
//     */
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    /**
//     * 계정 잠금 여부
//     * @return ture = 잠기지 않음
//     */
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    /**
//     * 비밀번호 만료 여부
//     * @return true = 만료 안됨
//     */
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    /**
//     * 계정 활성화 여부
//     * @return ture = 활성화 됨
//     */
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
