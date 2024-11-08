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
    public boolean isLessThan(Quantity other) {
        return this.value < other.value;
    }

    public Quantity subtract(Quantity other) {
        if (this.value < other.value) {
            throw new IllegalArgumentException("[ERROR] 차감할 수량이 현재 수량보다 많습니다.");
        }
        return new Quantity(this.value - other.value);
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }
}
