package store.dto.response;

import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.product.Stock;
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

    private static String formatPrice(Price price) {
        return String.format("%,d원", price.value());
    }

    private static String formatStock(Quantity quantity) {
        if (quantity.value() == 0) {
            return "재고 없음";
        }
        return quantity.value() + "개";
    }

    private static String getPromotionName(Product product) {
        if (!product.isPromotionProduct()) {
            return "";
        }
        return ((PromotionProduct) product).getPromotionName();
    }
}