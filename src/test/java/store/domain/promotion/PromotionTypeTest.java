package store.domain.promotion;

import org.junit.jupiter.api.Test;
import store.domain.vo.Quantity;

import static org.junit.jupiter.api.Assertions.*;

class PromotionTypeTest {

    @Test
    void 탄산음료이플러스일프로모션_적용여부확인() {
        PromotionType promotion = PromotionType.CARBONATED_DRINKS_TWO_PLUS_ONE;
        Quantity quantity = new Quantity(2);

        assertTrue(promotion.isApplicable(quantity), "2+1 프로모션은 2개 구매 시 적용되어야 합니다.");
    }

    @Test
    void 탄산음료이플러스일프로모션_증정아이템수계산() {
        PromotionType promotion = PromotionType.CARBONATED_DRINKS_TWO_PLUS_ONE;
        Quantity quantity = new Quantity(4);

        assertEquals(2, promotion.calculateFreeItems(quantity), "4개 구매 시 2개의 증정 아이템이 있어야 합니다.");
    }
    @Test
    void 엠디추천프로모션_증정아이템수계산() {
        PromotionType promotion = PromotionType.MD_RECOMMENDED;
        Quantity quantity = new Quantity(2);

        assertEquals(2, promotion.calculateFreeItems(quantity),
                "2개 구매 시 2개의 증정 아이템이 제공되어야 합니다.");
    }

    @Test
    void 번개세일_증정아이템수계산() {
        PromotionType promotion = PromotionType.FLASH_SALE;
        Quantity quantity = new Quantity(3);

        assertEquals(3, promotion.calculateFreeItems(quantity),
                "3개 구매 시 3개의 증정 아이템이 제공되어야 합니다.");
    }

}