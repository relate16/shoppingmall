package com.example.shoppingmall.repository;

import com.example.shoppingmall.dto.ItemDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ItemQueryRepositoryTest {

    @Autowired
    ItemQueryRepository itemQueryRepository;

    @Test
    void showPageList() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ItemDto> itemDtos = itemQueryRepository.showPageList(pageRequest);

        Assertions.assertThat(itemDtos.getSize()).isEqualTo(10);
        Assertions.assertThat(itemDtos.getContent().get(0).getPrice()).isEqualTo(50);
        Assertions.assertThat(itemDtos.getContent().get(1).getPrice()).isEqualTo(100);
        Assertions.assertThat(itemDtos.getContent().get(2).getPrice()).isEqualTo(150);
    }
}
