package com.example.shoppingmall.repository;

import com.example.shoppingmall.dto.PostDto;
import com.example.shoppingmall.dto.PostSearchCondition;
import com.example.shoppingmall.dto.QPostDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.shoppingmall.entity.QPost.post;

@Repository
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PostQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<PostDto> showPageList(Pageable pageable) {
        List<PostDto> content = queryFactory
                .select(new QPostDto(post.id, post.title, post.content, post.writer, post.read,
                        post.createdDate, post.lastModifiedDate))
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc().nullsLast()).fetch();

        JPAQuery<Long> countQuery = queryFactory.select(post.count()).from(post);

        return PageableExecutionUtils.getPage(content, pageable,countQuery::fetchOne);

    }


    public Page<PostDto> searchPostDtos(PostSearchCondition postSearchCondition, Pageable pageable) {
        List<PostDto> content = queryFactory.select(
                        new QPostDto(post.id, post.title, post.content, post.writer, post.read, post.createdDate, post.lastModifiedDate))
                .from(post)
                .where(containTitle(postSearchCondition.getTitle()),
                        containContent(postSearchCondition.getContent()),
                        containWriter(postSearchCondition.getWriter()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc().nullsLast())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(post.count())
                .from(post)
                .where(containTitle(postSearchCondition.getTitle()),
                        containContent(postSearchCondition.getContent()),
                        containWriter(postSearchCondition.getWriter()));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }

    private BooleanExpression containTitle(String title) {
        return title != null ? post.title.contains(title) : null;
    }

    private BooleanExpression containContent(String content) {
        return content != null? post.content.contains(content) : null;
    }

    private BooleanExpression containWriter(String writer) {
        return writer != null? post.writer.contains(writer) : null;
    }

}
