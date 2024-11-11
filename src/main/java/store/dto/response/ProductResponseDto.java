package store.dto.response;

import store.domain.product.Product;
import store.domain.product.PromotionProduct;
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
                product.getName()
        );
    }

    public boolean hasPromotion() {
        return !promotionName.isEmpty() && !promotionName.equals("null");
    }

    public String getStockDisplay() {
        if (stockStatus == 0) {
            // 프로모션 상품과 일반 상품 구분
            if (hasPromotion()) {
                return String.format("%s %s", stockStatus + "개", promotionName);
            }
            return "재고 없음";
        }
        // 재고가 있는 경우
        if (hasPromotion()) {
            return String.format("%s %s", stockStatus + "개", promotionName);
        }
        return stockStatus + "개";
    }
}