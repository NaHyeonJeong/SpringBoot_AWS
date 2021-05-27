package com.hj.springaws.config.auth;

import com.hj.springaws.config.auth.dto.OAuthAttributes;
import com.hj.springaws.config.auth.dto.SessionUser;
import com.hj.springaws.domain.user.User;
import com.hj.springaws.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * 구글 로그인 이후 가져온 사용자의 정보들을 기반
 * 가입, 정보수정, 세션 저장 등의 기능 지원
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration().getRegistrationId(); //현재 로그인 진행 중인 서비스를 구분하는 코드(네이버, 카카오, 구글 ?구분)
        String userNameAttributeName = userRequest //OAuth 2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
                .getClientRegistration().getProviderDetails() //구글의 경우 기본적으로 코드(sub) 지원, 네이버 카카오 등은 기본 지원 없음
                .getUserInfoEndpoint().getUserNameAttributeName(); //네이버, 구글 로그인 동시 지원 시 사용

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        //구글 사용자 정보가 업데이트 되었을 때를 대비
        User user = userRepository.findByEmail(attributes.getEmail()).map(entity ->
                entity.update(attributes.getName(), attributes.getPicture())).orElse(attributes.toEntity()
        );
        return userRepository.save(user);
    }
}