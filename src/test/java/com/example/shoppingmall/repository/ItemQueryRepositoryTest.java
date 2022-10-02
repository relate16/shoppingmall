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
        /* 테스트시 requestPage = 0 고정,
        * 테스트로 넣어둔 상품 고정 개수 10개이므로 상품 고정 개수를 변경하지 않는다면 itemTotalCount = 10 으로 고정 */
        int requestPage = 0; // 요청 페이지 , 무조건 0으로 고정
        int pageItemCount = 9; // 페이지당 보여줄 수 있는 item 개수
        int itemTotalCount = 10; // DB에 등록된 아이템 총 개수, 10으로 고정 (등록된 상품 개수를 변경시키면 유동적 변경 가능)
        int lastPage = itemTotalCount / pageItemCount; // 요청할 수 있는 끝 페이지
        int requestPageItemCount; // DB에 등록된 아이템 총 개수에 따라, 요청 페이지에 보여지는 아이템 개수

        while (requestPage < lastPage + 1) {
            PageRequest pageRequest = PageRequest.of(requestPage, pageItemCount);
            Page<ItemDto> itemDtos = itemQueryRepository.showPageList(pageRequest);

            /* requestPageItemCount는 DB에 등록된 아이템 총 개수에 따라 요청 페이지에 보여지는 아이템 개수이므로
             * pageItemCount = 9, itemTotalCount = 10 일 때,
             * requestPage = 0 에서는 requestPageItemCount = 9 이고
             * requestPage = 1 에서는 requestPageItemCount = 1 이게 된다.
             * */
            requestPageItemCount =
                    (itemTotalCount / (pageItemCount * (requestPage + 1)) >= 1) ?
                            9 : (itemTotalCount - (pageItemCount * requestPage));

            Assertions.assertThat(itemDtos.getNumberOfElements()).isEqualTo(requestPageItemCount);
            for (int i = 0; i < requestPageItemCount; i++) {
                if (requestPage == 0) {
                    Assertions.assertThat(itemDtos.getContent().get(i).getPrice())
                            .isEqualTo(50 * (i + 1));
                } else {
                    /* 2페이지일 경우 첫 반복문에
                    * price가 500이 되어야 하므로 .isEqualTo(50 * ((i +1) + pageItemCount * (requestPage)))이 됨 */
                    Assertions.assertThat(itemDtos.getContent().get(i).getPrice())
                            .isEqualTo(50 * ((i + 1) + pageItemCount * (requestPage)));
                }
            }
            requestPage += 1;
        }
    }

}
