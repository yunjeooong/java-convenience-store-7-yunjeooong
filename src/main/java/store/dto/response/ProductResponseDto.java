package store.dto.response;

import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public record ProductResponseDto(
        String name,
        long price,
        long stockStatus,
        String promotionName
) {
    public static ProductResponseDto from(Product product, Quantity stockQuantity) {
        return new ProductResponseDto(
                product.getName(),
                product.calculateTotalPrice(new Quantity(1)).value(),
                stockQuantity.value(),
                getPromotionName(product)
        );
    }

    private static String getPromotionName(Product product) {
        if (product instanceof PromotionProduct promotionProduct) {
            return promotionProduct.getPromotionName();
        }
        return "";
    }
}