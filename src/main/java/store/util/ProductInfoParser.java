package store.util;

import java.util.List;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class ProductInfoParser {

    public static ProductInfo parseProductInfo(String line) {
        List<String> parts = List.of(line.split(","));
        validateProductParts(parts);
        return createProductInfo(parts);
    }

    private static void validateProductParts(List<String> parts) {
        if (parts.size() != 4) {
            throw new IllegalArgumentException("[ERROR] 상품 데이터 형식이 올바르지 않습니다.");
        }
    }

    private static ProductInfo createProductInfo(List<String> parts) {
        return new ProductInfo(
                parts.get(0).trim(),
                new Price(Integer.parseInt(parts.get(1).trim())),
                new Quantity(Integer.parseInt(parts.get(2).trim())),
                parts.get(3).trim()
        );
    }

    public record ProductInfo(String name, Price price, Quantity quantity, String promotionName) {
        public boolean hasPromotion() {
            return !"null".equals(promotionName);
        }
    }
}