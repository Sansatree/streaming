package com.sstree.streaming.streamingserver.config.auth;

import com.sstree.streaming.streamingserver.jwt.exception.JwtAccessDeniedHandler;
import com.sstree.streaming.streamingserver.jwt.exception.JwtAuthenticationEntryPointer;
import com.sstree.streaming.streamingserver.jwt.JwtSecurityConfig;
import com.sstree.streaming.streamingserver.jwt.JwtTokenProvider;
import com.sstree.streaming.streamingserver.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //스프링 빈에 등록하고 설정 클래스임을 나타낸다.
@EnableWebSecurity //Spring Security 활성화
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailServiceImpl;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPointer jwtAuthenticationEntryPointer;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//
//    @Override
//    //인증 무시할 경로
//    public void configure(WebSecurity web) throws Exception {
////        web.ignoring().antMatchers("/css/**", "/js/**" );
//    }


    @Override
    //http 관련 인증 설정 가능
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//csrf 설정 disable
                // exception handling class
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPointer)

                //h2-console 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // token 사용을 위해 세션 설정 stateless
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/signup","/","home","/login*").permitAll() // 아무나 접근 가능
                .antMatchers("/livestream/hls").hasAnyRole("USER") // USER, ADMIN 만 접근 가능
                .anyRequest().authenticated()// 나머지 요청은 권한의 종류에 상관없이 권한이 있어야 접근 가능

                .and()
                .formLogin()
                .loginPage("/login-page") //사용자가 만든 로그인 페이지
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login-page") //로그인 인증 처리 하는 URL
                .defaultSuccessUrl("/livestream/hls") //로그인 성공시 이동할 페이지

//                .and()
//                .logout()
//                .logoutSuccessUrl("/login-page") // 로그아웃시 리다이렉트 URL
//                .invalidateHttpSession(true)// 세션 없애기

                // JwtSecurityConfig 클래스 적용
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

//
//        http
//                .csrf().disable()
//                .authorizeRequests(authorize -> authorize
//                        .antMatchers("/login-page","/signup").permitAll() // 아무나 접근 가능
//                        .antMatchers("/hls").hasAnyRole("USER") // USER, ADMIN만 접근 가능
//                        .anyRequest().authenticated()) // 나머지 요청은 권한의 종류에 상관없이 권한이 있어야 접근 가능
//
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login-page") //사용자가 만든 로그인 페이지
//                        .loginProcessingUrl("/login-process") //로그인 인증 처리 하는 URL
//                        .defaultSuccessUrl("/hls")) //로그인 성공시 이동할 페이지
////                        .failureUrl() //인증 실패시 이동할 URL
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/login-page") // 로그아웃시 리다이렉트 URL
//                        .invalidateHttpSession(true) // 세션 없애기
//                );

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
    }
}
