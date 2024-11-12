package store.util;

import java.text.NumberFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.dto.response.ProductResponseDto;

public class ProductFormatter {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();
    private static final String CURRENCY_UNIT = "원";
    private static final String PRODUCT_PREFIX = "-";

    private ProductFormatter() {
    }

    public static String formatProductInfo(ProductResponseDto product) {
        return Stream.of(
                        PRODUCT_PREFIX,
                        product.name(),
                        formatPrice(product.price()),
                        product.getStockDisplay(),
                        product.getPromotionDisplay()
                )
                .filter(str -> !str.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public static String formatPrice(long price) {
        return NUMBER_FORMAT.format(price) + CURRENCY_UNIT;
    }
}