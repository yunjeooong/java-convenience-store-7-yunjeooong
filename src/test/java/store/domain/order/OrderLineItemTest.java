package store.domain.order;

import org.junit.jupiter.api.Test;
import store.domain.vo.Money;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.promotion.PromotionType;
import store.domain.stock.Stocks;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemTest {
    @Test
    void 일플러스일_증정아이템_생성_테스트() {
        Stocks stocks = Stocks.of(new Quantity(10), new Quantity(10));
        PromotionProduct promotionProduct = new PromotionProduct(
                "초코바",
                new Price(1200),
                stocks,
                PromotionType.MD_RECOMMENDED
        );
        OrderLineItem orderLineItem = OrderLineItem.create(promotionProduct, new Quantity(2));

        OrderLineItem freeItem = orderLineItem.createFreeItemLine();

        assertNotNull(freeItem, "증정 아이템이 생성되어야 합니다.");
        assertEquals(new Quantity(2), freeItem.getQuantity(),
                "2개 구매 시 2개의 증정 아이템이 제공되어야 합니다.");
    }

    @Test
    void 일플러스일_큰수량_증정아이템_생성_테스트() {
        Stocks stocks = Stocks.of(new Quantity(10), new Quantity(10));
        PromotionProduct promotionProduct = new PromotionProduct(
                "초코바",
                new Price(1200),
                stocks,
                PromotionType.MD_RECOMMENDED
        );
        OrderLineItem orderLineItem = OrderLineItem.create(promotionProduct, new Quantity(4));
        OrderLineItem freeItem = orderLineItem.createFreeItemLine();

        assertNotNull(freeItem, "증정 아이템이 생성되어야 합니다.");
        assertEquals(new Quantity(4), freeItem.getQuantity(),
                "4개 구매 시 4개의 증정 아이템이 제공되어야 합니다.");
    }

    @Test
    void 일플러스일_할인금액_테스트() {
        Stocks stocks = Stocks.of(new Quantity(10), new Quantity(10));
        PromotionProduct promotionProduct = new PromotionProduct(
                "초코바",
                new Price(1200),
                stocks,
                PromotionType.MD_RECOMMENDED
        );
        OrderLineItem orderLineItem = OrderLineItem.create(promotionProduct, new Quantity(4));

        OrderLineItem freeItem = orderLineItem.createFreeItemLine();
        Money discount = freeItem.calculateItemPrice();

        assertEquals(new Money(4800), discount,
                "4개 구매 시 4개 금액만큼 할인되어야 합니다.");
    }
}