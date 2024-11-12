package store.domain.vo;

public record Price(int value) {
    public static final Price ZERO = new Price(0);

    private static final int MINIMUM_PRICE = 0;
    private static final String ERROR_NEGATIVE_PRICE = "[ERROR] 가격은 0원 이상이어야 합니다.";

    public Price {
        validatePrice(value);
    }

    private static void validatePrice(int value) {
        if (value < MINIMUM_PRICE) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_PRICE);
        }
    }

    public Price add(Price other) {
        return new Price(this.value + other.value);
    }

    public Price multiply(int quantity) {
        return new Price(this.value * quantity);
    }

    public Money toMoney() {
        return new Money(this.value);
    }
}