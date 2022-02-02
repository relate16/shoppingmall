package com.example.shoppingmall.repository;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.dto.QItemDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.shoppingmall.entity.QItem.item;

@Repository
public class ItemQueryRepository {

    private JPAQueryFactory queryFactory;

    public ItemQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 페이지당 아이템 리스트 보여주는 함수.
     */
    public Page<ItemDto> showPageList(Pageable pageable) {
        int limit = 9;
        int offset = (pageable.getPageNumber() * limit);

        List<ItemDto> content = queryFactory
                .select(new QItemDto(item.id, item.name, item.price, item.title, item.filePath))
                .from(item).offset(offset).limit(limit)
                .fetch();
        JPAQuery<Long> countQuery = queryFactory.select(item.count()).from(item);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    /**
     * 파라미터로 넘어온 id로 해당 id를 가진 item 정보 들고오는 함수.
     */
    public ItemDto findItemDtoById(Long id) {
        return queryFactory.select(
                        new QItemDto(item.id, item.name, item.price, item.title, item.filePath))
                .from(item).where(item.id.eq(id)).fetchOne();
    }



}
