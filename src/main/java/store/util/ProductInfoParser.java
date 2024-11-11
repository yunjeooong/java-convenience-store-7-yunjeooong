package store.util;

import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class ProductInfoParser {
    private static final String DELIMITER = ",";
    private static final int EXPECTED_PARTS_LENGTH = 4;
    private static final String ERROR_INVALID_FORMAT = "[ERROR] 상품 데이터 형식이 올바르지 않습니다.";
    private static final String NULL_PROMOTION = "null";

    private ProductInfoParser() {
    }

    public static ProductInfo parseProductInfo(String line) {
        String[] parts = line.split(DELIMITER);
        validateProductParts(parts);
        return createProductInfo(parts);
    }

    private static void validateProductParts(String[] parts) {
        if (parts.length != EXPECTED_PARTS_LENGTH) {
            throw new IllegalArgumentException(ERROR_INVALID_FORMAT);
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
            return !NULL_PROMOTION.equals(promotionName);
        }
    }
}