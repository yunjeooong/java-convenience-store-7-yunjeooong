package store.validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.vo.Quantity;

public class InputValidator {
    private static final String ITEM_REGEX = "\\[([^-]+-\\d+)\\]";
    private static final Pattern ITEM_PATTERN = Pattern.compile(ITEM_REGEX);
    private static final String YES = "Y";
    private static final String NO = "N";

    private InputValidator() {
    }

    public static List<PurchaseItem> validatePurchaseInput(String input) {
        validateInput(input);
        List<PurchaseItem> items = new ArrayList<>();

        Matcher matcher = ITEM_PATTERN.matcher(input);
        while (matcher.find()) {
            items.add(parsePurchaseItem(matcher.group(1)));
        }

        validateNotEmpty(items);
        validateNoDuplicates(items);
        return items;
    }

    private static void validateInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 입력이 비어있습니다.");
        }
    }

    private static PurchaseItem parsePurchaseItem(String itemInfo) {
        String[] parts = itemInfo.split("-");
        validateParts(parts);

        String name = validateName(parts[0].trim());
        Quantity quantity = validateQuantity(parts[1].trim());

        return new PurchaseItem(name, quantity);
    }

    private static void validateParts(String[] parts) {
        if (parts.length != 2) {
            throw new IllegalArgumentException("[ERROR] 상품 정보가 올바르지 않습니다. 상품명-수량 형식으로 입력해주세요.");
        }
    }

    private static String validateName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품명은 비어있을 수 없습니다.");
        }
        return name;
    }

    private static Quantity validateQuantity(String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new IllegalArgumentException("[ERROR] 수량은 1개 이상이어야 합니다.");
            }
            return new Quantity(quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 수량은 숫자여야 합니다.");
        }
    }

    private static void validateNotEmpty(List<PurchaseItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 최소 1개 이상의 상품을 입력해주세요.");
        }
    }

    private static void validateNoDuplicates(List<PurchaseItem> items) {
        Set<String> names = new HashSet<>();
        for (PurchaseItem item : items) {
            if (!names.add(item.productName())) {
                throw new IllegalArgumentException(
                        String.format("[ERROR] 중복된 상품이 있습니다: %s", item.productName()));
            }
        }
    }

    public static void validateYesNo(String input) {
        if (!YES.equals(input) && !NO.equals(input)) {
            throw new IllegalArgumentException("[ERROR] Y 또는 N으로 입력해주세요.");
        }
    }

    public record PurchaseItem(String productName, Quantity quantity) {
    }
}
