package store.domain.promotion;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import store.domain.vo.Quantity;

public enum PromotionType {
    CARBONATED_DRINKS_TWO_PLUS_ONE("탄산2+1", 2, 1,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 31)),
    MD_RECOMMENDED("MD추천상품", 1, 1,
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

    public static PromotionType from(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }


    private boolean isWithinPromotionPeriod() {
        LocalDateTime currentDateTime = DateTimes.now();
        LocalDate currentDate = currentDateTime.toLocalDate();
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }
    public boolean isApplicable(Quantity quantity) {
        return isWithinPromotionPeriod() &&
                quantity.value() >= this.buyQuantity;
    }

    public int calculateFreeItems(Quantity quantity) {
        if (!isApplicable(quantity)) {
            return 0;
        }
        if (this == MD_RECOMMENDED || this == FLASH_SALE) {
            return quantity.value() / 2;
        }
        return (quantity.value() / this.buyQuantity) * this.freeQuantity;
    }


}