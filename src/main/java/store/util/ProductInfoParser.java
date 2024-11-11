package store.util;

import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class ProductInfoParser {
    private ProductInfoParser() {
    }

    public static ProductInfo parseProductInfo(String line) {
        String[] parts = line.split(",");
        validateProductParts(parts);
        return createProductInfo(parts);
    }

    private static void validateProductParts(String[] parts) {
        if (parts.length != 4) {
            throw new IllegalArgumentException("[ERROR] 상품 데이터 형식이 올바르지 않습니다.");
        }
    }

    private static ProductInfo createProductInfo(String[] parts) {
        return new ProductInfo(
                parts[0].trim(),
                new Price(Integer.parseInt(parts[1].trim())),
                new Quantity(Integer.parseInt(parts[2].trim())),
                parts[3].trim()
        );
    }

    public record ProductInfo(String name, Price price, Quantity quantity, String promotionName) {
        public boolean hasPromotion() {
            return !"null".equals(promotionName);
        }
    }
}