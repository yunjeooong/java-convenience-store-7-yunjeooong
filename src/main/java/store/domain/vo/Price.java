package store.domain.vo;

public record Price(int value) {
    public static final Price ZERO = new Price(0);

    public Price {
        validatePrice(value);
    }

    private static void validatePrice(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("[ERROR] 가격은 0원 이상이어야 합니다.");
        }
    }
}
