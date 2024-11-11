package store.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.vo.Quantity;

public class InputValidator {
    private static final String ITEM_REGEX = "\\[\\s*([^-]+)\\s*-\\s*(\\d+)\\s*\\]";
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
            String name = matcher.group(1).trim();
            String quantityStr = matcher.group(2).trim();
            Quantity quantity = validateQuantity(quantityStr);
            validateName(name);
            items.add(new PurchaseItem(name, quantity));
        }

        validateNotEmpty(items);
        return items;
    }

    private static void validateInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_FORMAT.getMessage());
        }
    }

    private static String validateName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_FORMAT.getMessage());
        }
        return name;
    }

    private static Quantity validateQuantity(String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new IllegalArgumentException(ErrorMessages.INVALID_FORMAT.getMessage());
            }
            return new Quantity(quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_FORMAT.getMessage());
        }
    }

    private static void validateNotEmpty(List<PurchaseItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_FORMAT.getMessage());
        }
    }

    public static void validateYesNo(String input) {
        if (!YES.equals(input) && !NO.equals(input)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_INPUT.getMessage());
        }
    }

    public record PurchaseItem(String productName, Quantity quantity) {
    }

    public enum ErrorMessages {
        INVALID_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
        PRODUCT_NOT_FOUND("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
        INSUFFICIENT_STOCK("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
        INVALID_INPUT("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        private final String message;

        ErrorMessages(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}