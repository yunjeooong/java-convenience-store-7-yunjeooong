package store.dto.request;

import java.util.HashMap;
import java.util.Map;
import store.domain.vo.Quantity;

public record CartRequestDto(Map<String, Quantity> items) {
    public CartRequestDto {
        validateItems(items);
        items = new HashMap<>(items);
    }

    private static void validateItems(Map<String, Quantity> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품은 필수입니다.");
        }
    }

    public static CartRequestDto from(Map<String, Quantity> items) {
        return new CartRequestDto(items);
    }
}
