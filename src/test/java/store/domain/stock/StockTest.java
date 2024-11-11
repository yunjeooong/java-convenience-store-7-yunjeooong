package store.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.vo.Quantity;

class StocksTest {
    private Stocks stocks;
    private static final String ERROR_INSUFFICIENT_STOCK = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    @BeforeEach
    void setUp() {
        stocks = Stocks.of(new Quantity(10), new Quantity(5));
    }

    @Test
    @DisplayName("재고를 생성할 수 있다")
    void createStocks() {
        assertThat(stocks.getRegularStock().getQuantity().value()).isEqualTo(10);
        assertThat(stocks.getPromotionStock().getQuantity().value()).isEqualTo(5);
    }

    @ParameterizedTest
    @DisplayName("일반 재고를 제거할 수 있다")
    @CsvSource({"3", "5", "10"})
    void removeRegularStock(int amount) {
        stocks.removeRegularStock(new Quantity(amount));

        assertThat(stocks.getRegularStock().getQuantity().value()).isEqualTo(10 - amount);
    }

    @Test
    @DisplayName("일반 재고가 부족하면 예외가 발생한다")
    void removeRegularStockWithInsufficientStock() {
        assertThatThrownBy(() -> stocks.removeStock(new Quantity(11), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_INSUFFICIENT_STOCK);
    }

    @Test
    @DisplayName("프로모션 재고를 제거할 수 있다")
    void removePromotionStock() {
        stocks.removePromotionStock(new Quantity(3));

        assertThat(stocks.getPromotionStock().getQuantity().value()).isEqualTo(2);
    }

    @Test
    @DisplayName("프로모션 재고가 부족하면 일반 재고에서 차감한다")
    void removePromotionStockWithInsufficientStock() {
        stocks.removePromotionStock(new Quantity(7));

        assertThat(stocks.getPromotionStock().getQuantity().value()).isEqualTo(0);
        assertThat(stocks.getRegularStock().getQuantity().value()).isEqualTo(8);
    }

    @Test
    @DisplayName("전체 재고(프로모션 + 일반)가 부족하면 예외가 발생한다")
    void removeStockWithInsufficientTotalStock() {
        assertThatThrownBy(() -> stocks.removeStock(new Quantity(16), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_INSUFFICIENT_STOCK);
    }

    @ParameterizedTest
    @DisplayName("프로모션 재고 충분 여부를 확인할 수 있다")
    @CsvSource({"5,true", "6,false"})
    void hasEnoughPromotionStock(int amount, boolean expected) {
        assertThat(stocks.hasEnoughPromotionStock(new Quantity(amount))).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("일반 재고 충분 여부를 확인할 수 있다")
    @CsvSource({"10,true", "11,false"})
    void hasEnoughRegularStock(int amount, boolean expected) {
        assertThat(stocks.hasEnoughRegularStock(new Quantity(amount))).isEqualTo(expected);
    }

    @Test
    @DisplayName("프로모션 여부에 따라 적절한 재고를 반환한다")
    void getStock() {
        assertThat(stocks.getStock(true)).isInstanceOf(PromotionStock.class);
        assertThat(stocks.getStock(false)).isInstanceOf(RegularStock.class);
    }
}