package store.domain.vo;

public record Quantity(int value) {
    public static final Quantity ZERO = new Quantity(0);

    private static final int MINIMUM_QUANTITY = 0;
    private static final String ERROR_NEGATIVE_QUANTITY = "[ERROR] 수량은 0 이상이어야 합니다.";
    private static final String ERROR_INSUFFICIENT_QUANTITY = "[ERROR] 차감할 수량이 현재 수량보다 많습니다.";

    public Quantity {
        validateQuantity(value);
    }

    private static void validateQuantity(int value) {
        if (value < MINIMUM_QUANTITY) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_QUANTITY);
        }
    }

    public boolean isLessThan(Quantity other) {
        return this.value < other.value;
    }

    public Quantity subtract(Quantity other) {
        if (this.value < other.value) {
            throw new IllegalArgumentException(ERROR_INSUFFICIENT_QUANTITY);
        }
        return new Quantity(this.value - other.value);
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }
}