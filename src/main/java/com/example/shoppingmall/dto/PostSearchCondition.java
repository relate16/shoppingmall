package com.example.shoppingmall.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSearchCondition {
    private String title;
    private String writer;
    private String content;

    @QueryProjection
    public PostSearchCondition(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }
}
