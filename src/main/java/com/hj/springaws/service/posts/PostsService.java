package com.hj.springaws.service.posts;

import com.hj.springaws.domain.posts.Posts;
import com.hj.springaws.domain.posts.PostsRepository;
import com.hj.springaws.web.dto.PostsResponseDto;
import com.hj.springaws.web.dto.PostsSaveRequestDto;
import com.hj.springaws.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * update()에 쿼리를 날리는 부분이 없는 이유는...
 * JPA의 영속성 컨텍스트 때문
 * 영속성 컨텍스트 : entity를 영구 저장하는 환경
 */
@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id)
        );
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id)
        );
        return new PostsResponseDto(entity);
    }
}
