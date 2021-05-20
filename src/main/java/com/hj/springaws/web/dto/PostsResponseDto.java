package com.hj.springaws.web.dto;

import com.hj.springaws.domain.posts.Posts;
import lombok.Getter;

/**
 * Entity의 필드 중 일부만 사용
 */
@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
