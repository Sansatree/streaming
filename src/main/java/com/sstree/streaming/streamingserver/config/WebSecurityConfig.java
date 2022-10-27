package com.sstree.streaming.streamingserver.config;

import com.sstree.streaming.streamingserver.exception.jwt.JwtAccessDeniedHandler;
import com.sstree.streaming.streamingserver.exception.jwt.JwtAuthenticationEntryPointer;
import com.sstree.streaming.streamingserver.jwt.JwtFilter;
import com.sstree.streaming.streamingserver.jwt.JwtTokenProvider;
import com.sstree.streaming.streamingserver.repository.RefreshTokenRepository;
import com.sstree.streaming.streamingserver.service.SuccessHandler;
import com.sstree.streaming.streamingserver.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration //스프링 빈에 등록하고 설정 클래스임을 나타낸다.
@EnableWebSecurity //Spring Security 활성화
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailServiceImpl;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPointer jwtAuthenticationEntryPointer;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final SuccessHandler successHandler;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    //인증 무시할 경로
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/h2-console/**");
    }


    @Override
    //http 관련 인증 설정 가능
    protected void configure(final HttpSecurity http) throws Exception {

        http    .csrf().disable()//csrf 설정 disable
                .cors()
                // exception handling class
                .and()
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
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/signup", "/home", "/login", "/h2-console/**","/auth/**").permitAll() // 아무나 접근 가능
                .antMatchers("/livestream/hls","livestream/hls").hasRole("USER") // USER, ADMIN 만 접근 가능
                .antMatchers(HttpMethod.OPTIONS, "/*").permitAll() //cors option 허용
                .anyRequest().authenticated()// 나머지 요청은 권한의 종류에 상관없이 권한이 있어야 접근 가능

                .and()
                .formLogin()
                .loginPage("/login") //사용자가 만든 로그인 페이지
                .loginProcessingUrl("/auth/login") //로그인 인증 처리 하는 URL
                .failureUrl("/login")
                .successHandler(successHandler)





                .and()
                .addFilterAfter(jwtFilter(),UsernamePasswordAuthenticationFilter.class);


//                .and()
//                .logout()
//                .logoutSuccessUrl("/login") // 로그아웃시 리다이렉트 URL
//                .invalidateHttpSession(true)// 세션 없애기



    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(jwtTokenProvider,refreshTokenRepository);
    }


}
