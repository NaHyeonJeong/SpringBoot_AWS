package com.hj.springaws.config.auth;

import com.hj.springaws.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //Spring Security 설정들을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable().headers().frameOptions().disable() //h2-console 화면 사용 위해 해당 옵션들 disable()
                .and()
                .authorizeRequests() //URL별 권한 관리 설정하는 옵션 시작 || antMatchers() : 권한 관리 대상 지정 옵션, URL/HTTP 메소드별로 관리 가능
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() //전체 열람 권한
                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //USER 권한 가진 사람만
                .anyRequest().authenticated() //설정된 값들 이외 나머지 URL들을 나타냄, 나머지 URL들은 모두 인증된 사용자들에게만 허용(로그인한 사용자들)
                .and().logout()
                .logoutSuccessUrl("/") //로그아웃 기능에 대한 여러 설정의 진입점, 로그아웃 성공시 /주소로 이동
                .and()
                .oauth2Login() //OAuth 2 로그인 기능에 대한 여러 설정의 진입점
                .userInfoEndpoint() //OAuth 2 로그인 성공 이후 사용자 정보 가져올 때의 설정들 담당
                .userService(customOAuth2UserService); //소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
    }
}
