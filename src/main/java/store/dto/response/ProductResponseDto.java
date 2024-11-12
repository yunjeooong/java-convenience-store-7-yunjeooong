package store.dto.response;

import store.domain.product.Product;
import store.domain.vo.Quantity;

public record ProductResponseDto(
        String name,
        long price,
        long stockStatus,
        String promotionName
) {
    private static final String NO_STOCK_MESSAGE = "재고 없음";
    private static final String NULL_PROMOTION = "null";
    private static final String STOCK_UNIT = "개";

    public static ProductResponseDto from(Product product, Quantity quantity) {
        return new ProductResponseDto(
                product.getName(),
                product.getPrice().value(),
                quantity.value(),
                product.promotionName()
        );
    }

    public boolean hasPromotion() {
        return promotionName != null && !promotionName.equals(NULL_PROMOTION);
    }

    public String getStockDisplay() {
        if (stockStatus == 0) {
            return NO_STOCK_MESSAGE;
        }
        return stockStatus + STOCK_UNIT;
    }

    public String getPromotionDisplay() {
        if (hasPromotion()) {
            return promotionName;
        }
        return "";
    }
}
