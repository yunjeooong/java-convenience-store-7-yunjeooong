package store.dto.response;

import store.domain.product.Product;
import store.domain.vo.Quantity;

public record ProductResponseDto(
        String name,
        long price,
        long stockStatus,
        String promotionName
) {
    public static ProductResponseDto from(Product product, Quantity quantity) {
        return new ProductResponseDto(
                product.getName(),
                product.getPrice().value(),
                quantity.value(),
                product.promotionName()

        );
    }

    public boolean hasPromotion() {
        return promotionName != null && !promotionName.equals("null");
    }

    public String getStockDisplay() {
        if (stockStatus == 0) {
            return "재고 없음";
        }
        return stockStatus + "개";
    }

    public String getPromotionDisplay() {
        return hasPromotion() ? promotionName : "";
    }
}