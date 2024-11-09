package store.domain.vo;

public record Money(int value) {
    public static final Money ZERO = new Money(0);

    public Money {
        validateMoney(value);
    }

    private static void validateMoney(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("[ERROR] 지불 금액은 0원 이상이어야 합니다.");
        }
    }

    public Money subtract(Money other) {
        if (this.value < other.value) {
            throw new IllegalArgumentException("[ERROR] 잔액이 부족합니다.");
        }
        return new Money(this.value - other.value);
    }

    public Money add(Money other) {
        return new Money(this.value + other.value);
    }

    public Money multiply(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("[ERROR] 곱할 값은 0 이상이어야 합니다.");
        }
        return new Money(this.value * multiplier);
    }


    public String format() {
        return String.format("%,d", value);
    }
}
