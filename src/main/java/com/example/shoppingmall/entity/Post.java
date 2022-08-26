package com.example.shoppingmall.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;
    private String writer;
    private int read;

    public Post(String title, String content, String writer, int read) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.read = read;
    }

    //비지니스 로직
    public void addRead() {
        this.read += 1;
    }
}
