package store.domain.promotion;

import java.time.LocalDate;
import java.util.Arrays;
import store.domain.vo.Quantity;

public enum PromotionType {
    CARBONATED_DRINKS_TWO_PLUS_ONE("탄산2+1", 2, 1,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 31)),
    MD_RECOMMEND("MD추천상품", 1, 1,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 31)),
    FLASH_SALE("반짝할인", 1, 1,
            LocalDate.of(2024, 11, 1),
            LocalDate.of(2024, 11, 30));

    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    PromotionType(String name, int buyQuantity, int freeQuantity,
                  LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isApplicable(Quantity quantity) {
        return quantity.value() >= buyQuantity && isWithinPromotionPeriod();
    }

    public Quantity calculateFreeItems(Quantity purchaseQuantity) {
        if (!isApplicable(purchaseQuantity)) {
            return Quantity.ZERO;
        }
        return new Quantity((purchaseQuantity.value() / buyQuantity) * freeQuantity);
    }

    public Quantity calculateRequiredQuantity(Quantity currentQuantity) {
        if (isApplicable(currentQuantity)) {
            return Quantity.ZERO;
        }
        return new Quantity(buyQuantity - currentQuantity.value());
    }

    private boolean isWithinPromotionPeriod() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    public String getName() {
        return name;
    }

    public static PromotionType from(String name) {
        return Arrays.stream(values())
                .filter(promotion -> promotion.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}