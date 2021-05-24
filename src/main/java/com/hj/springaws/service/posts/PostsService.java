package com.hj.springaws.service.posts;

import com.hj.springaws.domain.posts.Posts;
import com.hj.springaws.domain.posts.PostsRepository;
import com.hj.springaws.web.dto.PostsListResponseDto;
import com.hj.springaws.web.dto.PostsResponseDto;
import com.hj.springaws.web.dto.PostsSaveRequestDto;
import com.hj.springaws.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id)
        );
        postsRepository.delete(posts); //JpaRepository 에서 이미 delete 메소드 지원 중, 활용하기
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id)
        );
        return new PostsResponseDto(entity);
    }

    //트랜잭션 범위는 유지하되, 조회 기능만 남겨 조회 속도 개선
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }

}
