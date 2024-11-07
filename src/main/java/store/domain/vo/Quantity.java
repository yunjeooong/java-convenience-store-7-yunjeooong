package store.domain.vo;

public record Quantity(int value) {
    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        validateQuantity(value);
    }

    private static void validateQuantity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("[ERROR] 수량은 0 이상이어야 합니다.");
        }
    }
}
