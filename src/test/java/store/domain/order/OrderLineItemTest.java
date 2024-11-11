/*
package store.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.product.RegularProduct;
import store.domain.vo.Money;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.domain.product.PromotionProduct;
import store.domain.promotion.PromotionType;
import store.domain.stock.Stocks;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemTest {
    private PromotionProduct promotionProduct;
    private OrderLineItem orderLineItem;
    private Stocks stocks;

    @BeforeEach
    void setUp() {
        stocks = Stocks.of(new Quantity(5), new Quantity(5)); // 프로모션 재고 5, 일반 재고 5
        promotionProduct = new PromotionProduct("초코바", new Price(1200), stocks, PromotionType.MD_RECOMMENDED);
        orderLineItem = OrderLineItem.create(promotionProduct, new Quantity(2));
    }

    @Test
    void 프로모션_상품_재고_차감_테스트() {
        Quantity initialPromotionStock = stocks.getPromotionStock().getQuantity();
        Quantity initialRegularStock = stocks.getRegularStock().getQuantity();

        orderLineItem.removeStock();

        Quantity expectedPromotionStock = initialPromotionStock.subtract(new Quantity(4));
        assertEquals(expectedPromotionStock, stocks.getPromotionStock().getQuantity(),
                "프로모션 재고가 4개 차감되어야 합니다.");
        assertEquals(initialRegularStock, stocks.getRegularStock().getQuantity(),
                "일반 재고는 변경되지 않아야 합니다.");
    }

    @Test
    void 프로모션_재고_부족시_일반재고_사용_테스트() {

        OrderLineItem largeOrderItem = OrderLineItem.create(promotionProduct, new Quantity(4));

        largeOrderItem.removeStock();

        assertEquals(Quantity.ZERO, stocks.getPromotionStock().getQuantity(),
                "프로모션 재고가 모두 소진되어야 합니다.");
        Quantity expectedRegularStock = new Quantity(2);
        assertEquals(expectedRegularStock, stocks.getRegularStock().getQuantity(),
                "부족한 수량만큼 일반 재고에서 차감되어야 합니다.");
    }

    @Test
    void 일반_상품_재고_차감_테스트() {
        Stocks regularStocks = Stocks.of(new Quantity(5), Quantity.ZERO);
        Product regularProduct = new RegularProduct("물", new Price(500), regularStocks);
        OrderLineItem regularOrderItem = OrderLineItem.create(regularProduct, new Quantity(2));

        regularOrderItem.removeStock();

        assertEquals(new Quantity(3), regularStocks.getRegularStock().getQuantity(),
                "일반 상품은 일반 재고에서만 차감되어야 합니다.");
    }

    @Test
    void 재고_부족시_예외_발생_테스트() {
        OrderLineItem largeOrderItem = OrderLineItem.create(promotionProduct, new Quantity(8));

        assertThrows(IllegalArgumentException.class,
                largeOrderItem::removeStock,
                "재고가 부족할 경우 예외가 발생해야 합니다.");
    }

    @Test
    void 일플러스일_증정아이템_생성_테스트() {
        OrderLineItem freeItem = orderLineItem.createFreeItemLine();

        assertNotNull(freeItem, "증정 아이템이 생성되어야 합니다.");
        assertEquals(new Quantity(2), freeItem.getQuantity(), "2개 구매 시 2개의 증정 아이템이 제공되어야 합니다.");
    }

    @Test
    void 일플러스일_큰수량_증정아이템_생성_테스트() {
        OrderLineItem orderLineItemWithMoreQuantity = OrderLineItem.create(promotionProduct, new Quantity(4));
        OrderLineItem freeItem = orderLineItemWithMoreQuantity.createFreeItemLine();

        assertNotNull(freeItem, "증정 아이템이 생성되어야 합니다.");
        assertEquals(new Quantity(4), freeItem.getQuantity(), "4개 구매 시 4개의 증정 아이템이 제공되어야 합니다.");
    }

    @Test
    void 일플러스일_할인금액_테스트() {
        OrderLineItem orderLineItemWithMoreQuantity = OrderLineItem.create(promotionProduct, new Quantity(4));
        OrderLineItem freeItem = orderLineItemWithMoreQuantity.createFreeItemLine();
        Money discount = freeItem.calculateItemPrice();

        assertEquals(new Money(4800), discount, "4개 구매 시 4개 금액만큼 할인되어야 합니다.");
    }

}
*/
