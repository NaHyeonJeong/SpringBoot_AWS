package com.hj.springaws.web;

import com.hj.springaws.config.auth.LoginUser;
import com.hj.springaws.config.auth.dto.SessionUser;
import com.hj.springaws.service.posts.PostsService;
import com.hj.springaws.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

/**
 * 메인 페이지와 관련된 컨트롤러
 */
@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
        //기존에 가져오던 세션 정보 값이 개선되었음
        //이제는 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보 가져올 수 있음

        model.addAttribute("posts", postsService.findAllDesc());

        //userName을 사용할 수 있게 userName을 model에 저장하는 코드 추가
        //로그인 성공 시 세션에 SessionUser를 저장하도록 구성
        //SessionUser user = (SessionUser) httpSession.getAttribute("user"); //로그인 성공시 값 가져올 수 있음

        if(user != null){
            //세션에 저장된 값이 있을 때만 model에 userName으로 등록
            //세션에 저장된 값이 없으면 model엔 아무런 값이 없는 상태이니 로그인 버튼이 보이게됨
            model.addAttribute("userName", user.getName());
        }

        return "index";//머스테치 스타터 덕분에 컨트롤러에서 문자열 반환시 앞의 경로와 확장자는 자동 지정
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        //@PathVariable : 말 그대로 URL 경로에 변수를 넣어주는 역할
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
}
