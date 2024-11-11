package store.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.product.RegularProduct;
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
        stocks = Stocks.of(new Quantity(5), new Quantity(5));
        promotionProduct = PromotionProduct.create(
                "초코바",
                new Price(1200),
                new Quantity(5),
                new Quantity(5),
                PromotionType.MD_RECOMMENDED
        );
        orderLineItem = OrderLineItem.create(promotionProduct, new Quantity(2));
    }

    @Test
    void 프로모션_상품_재고_차감_테스트() {
        Quantity initialPromotionStock = stocks.getPromotionStock().getQuantity();  // getQuantity() 추가
        Quantity initialRegularStock = stocks.getRegularStock().getQuantity();      // getQuantity() 추가

        orderLineItem.removeStock();

        assertEquals(initialPromotionStock.subtract(new Quantity(4)),
                stocks.getPromotionStock().getQuantity(),   // getQuantity() 추가
                "프로모션 재고가 4개 차감되어야 합니다.");
        assertEquals(initialRegularStock,
                stocks.getRegularStock().getQuantity(),     // getQuantity() 추가
                "일반 재고는 변경되지 않아야 합니다.");
    }

    @Test
    void 프로모션_재고_부족시_일반재고_사용_테스트() {
        OrderLineItem largeOrderItem = OrderLineItem.create(promotionProduct, new Quantity(4));

        largeOrderItem.removeStock();

        assertEquals(Quantity.ZERO,
                stocks.getPromotionStock(),
                "프로모션 재고가 모두 소진되어야 합니다.");
        assertEquals(new Quantity(2),
                stocks.getRegularStock(),
                "부족한 수량만큼 일반 재고에서 차감되어야 합니다.");
    }

    @Test
    void 일반_상품_재고_차감_테스트() {
        Stocks regularStocks = Stocks.of(new Quantity(5), Quantity.ZERO);
        Product regularProduct = RegularProduct.create(
                "물",
                new Price(500),
                new Quantity(5),
                Quantity.ZERO
        );
        OrderLineItem regularOrderItem = OrderLineItem.create(regularProduct, new Quantity(2));

        regularOrderItem.removeStock();

        assertEquals(new Quantity(3),
                regularStocks.getRegularStock(),
                "일반 상품은 일반 재고에서만 차감되어야 합니다.");
    }

    @Test
    void 재고_부족시_예외_발생_테스트() {
        OrderLineItem largeOrderItem = OrderLineItem.create(promotionProduct, new Quantity(8));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                largeOrderItem::removeStock,
                "재고가 부족할 경우 예외가 발생해야 합니다.");

        assertEquals("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.",
                exception.getMessage());
    }

    @Test
    void 일플러스일_증정아이템_생성_테스트() {
        OrderLineItem freeItem = orderLineItem.createFreeItemLine();

        assertNotNull(freeItem, "증정 아이템이 생성되어야 합니다.");
        assertEquals(new Quantity(2),
                freeItem.getQuantity(),
                "2개 구매 시 2개의 증정 아이템이 제공되어야 합니다.");
    }

    @Test
    void 일플러스일_큰수량_증정아이템_생성_테스트() {
        OrderLineItem orderLineItemWithMoreQuantity = OrderLineItem.create(promotionProduct, new Quantity(4));
        OrderLineItem freeItem = orderLineItemWithMoreQuantity.createFreeItemLine();

        assertNotNull(freeItem, "증정 아이템이 생성되어야 합니다.");
        assertEquals(new Quantity(4),
                freeItem.getQuantity(),
                "4개 구매 시 4개의 증정 아이템이 제공되어야 합니다.");
    }

}