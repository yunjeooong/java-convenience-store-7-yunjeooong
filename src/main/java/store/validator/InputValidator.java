package store.validator;

import java.util.ArrayList;
import java.util.List;
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
        return items;
    }

    private static void validateInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.EMPTY_INPUT.getMessage());
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
            throw new IllegalArgumentException(ErrorMessages.INVALID_ITEM_FORMAT.getMessage());
        }
    }

    private static String validateName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.EMPTY_PRODUCT_NAME.getMessage());
        }
        return name;
    }

    private static Quantity validateQuantity(String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new IllegalArgumentException(ErrorMessages.INVALID_QUANTITY.getMessage());
            }
            return new Quantity(quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessages.QUANTITY_NOT_NUMBER.getMessage());
        }
    }

    private static void validateNotEmpty(List<PurchaseItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.EMPTY_ITEMS.getMessage());
        }
    }

    public static void validateYesNo(String input) {
        if (!YES.equals(input) && !NO.equals(input)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_YES_NO_INPUT.getMessage());
        }
    }

    public record PurchaseItem(String productName, Quantity quantity) {
    }

    public enum ErrorMessages {
        EMPTY_INPUT("[ERROR] 입력이 비어있습니다. 다시 입력해 주세요."),
        INVALID_ITEM_FORMAT("[ERROR] 상품 정보가 올바르지 않습니다. 다시 입력해주세요."),
        EMPTY_PRODUCT_NAME("[ERROR] 상품명은 비어있을 수 없습니다. 다시 입력해주세요."),
        INVALID_QUANTITY("[ERROR] 수량은 1개 이상이어야 합니다."),
        QUANTITY_NOT_NUMBER("[ERROR] 수량은 숫자여야 합니다."),
        EMPTY_ITEMS("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
        INVALID_YES_NO_INPUT(" [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        private final String message;

        ErrorMessages(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}