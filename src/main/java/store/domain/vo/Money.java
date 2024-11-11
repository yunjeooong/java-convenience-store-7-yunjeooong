package store.domain.vo;

public record Money(int value) {
    public static final Money ZERO = new Money(0);

    private static final int MINIMUM_VALUE = 0;
    private static final String ERROR_NEGATIVE_AMOUNT = "[ERROR] 지불 금액은 0원 이상이어야 합니다.";
    private static final String ERROR_INSUFFICIENT_FUNDS = "[ERROR] 잔액이 부족합니다.";
    private static final String ERROR_NEGATIVE_MULTIPLIER = "[ERROR] 곱할 값은 0 이상이어야 합니다.";

    public Money {
        validateMoney(value);
    }

    private static void validateMoney(int value) {
        if (value < MINIMUM_VALUE) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_AMOUNT);
        }
    }

    public Money subtract(Money other) {
        if (this.value < other.value) {
            throw new IllegalArgumentException(ERROR_INSUFFICIENT_FUNDS);
        }
        return new Money(this.value - other.value);
    }

    public Money add(Money other) {
        return new Money(this.value + other.value);
    }

    public Money multiply(int multiplier) {
        if (multiplier < MINIMUM_VALUE) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_MULTIPLIER);
        }
        return new Money(this.value * multiplier);
    }

    public Money divide(int divisor) {
        return new Money(this.value / divisor);
    }

    public String format() {
        return String.format("%,d", value);
    }
}