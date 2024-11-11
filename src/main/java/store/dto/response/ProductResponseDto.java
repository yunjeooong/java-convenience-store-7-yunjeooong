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
    public static ProductResponseDto from(Product product, Quantity stockQuantity) {
        return new Builder()
                .withName(product.getName())
                .withPrice(product.calculateTotalPrice(new Quantity(1)).value())
                .withStockStatus(stockQuantity.value())
                .withPromotionName(getPromotionName(product))
                .build();
    }

    private static String getPromotionName(Product product) {
        if (product.isPromotionProduct()) {
            return ((PromotionProduct) product).promotionName();
        }
        return "";
    }

    public static class Builder {
        private String name;
        private long price;
        private long stockStatus;
        private String promotionName = "";

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPrice(long price) {
            this.price = price;
            return this;
        }

        public Builder withStockStatus(long stockStatus) {
            this.stockStatus = stockStatus;
            return this;
        }

        public Builder withPromotionName(String promotionName) {
            this.promotionName = promotionName;
            return this;
        }

        public ProductResponseDto build() {
            return new ProductResponseDto(name, price, stockStatus, promotionName);
        }
    }
}