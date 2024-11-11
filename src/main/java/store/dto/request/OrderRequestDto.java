package store.dto.request;

import store.domain.vo.Quantity;

public record OrderRequestDto(String productName, Quantity quantity) {
    private static final String ERROR_EMPTY_PRODUCT_NAME = "[ERROR] 상품명은 비어있을 수 없습니다.";

    public OrderRequestDto {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_PRODUCT_NAME);
        }
    }
}