package com.hj.springaws.config.auth.dto;

import com.hj.springaws.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * '세션에' 사용자 정보를 저장하기 위한 Dto 클래스
 * 인증된 사용자 정보만 필요
 * 왜 사용자 정보를 저장하는데 User 클래스를 사용하지 않고 새로 만든걸까?
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
