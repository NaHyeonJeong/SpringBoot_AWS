package com.hj.springaws.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메인 페이지와 관련된 컨트롤러
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(){
        return "index"; //머스테치 스타터 덕분에 컨트롤러에서 문자열 반환시 앞의 경로와 확장자는 자동 지정
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }
}
