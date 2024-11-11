package store.view;

import static store.validator.InputValidator.validateYesNo;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.vo.Quantity;
import store.dto.request.OrderRequestDto;
import store.validator.InputValidator;

public class InputView {
    private static final String PURCHASE_FORMAT_MESSAGE =
            "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP_MESSAGE =
            "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String ADDITIONAL_PURCHASE_MESSAGE =
            "구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String PROMOTION_SUGGESTION_FORMAT =
            "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String INSUFFICIENT_STOCK_CONFIRMATION_FORMAT =
            "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";


    public List<OrderRequestDto> readItems() {
        System.out.println(PURCHASE_FORMAT_MESSAGE);
        String input = Console.readLine();
        List<InputValidator.PurchaseItem> purchaseItems =
                InputValidator.validatePurchaseInput(input);

        return convertToOrderRequests(purchaseItems);
    }

    private List<OrderRequestDto> convertToOrderRequests(List<InputValidator.PurchaseItem> items) {
        return items.stream()
                .map(item -> new OrderRequestDto(item.productName(), item.quantity()))
                .collect(Collectors.toList());
    }

    public boolean readMembershipChoice() {
        System.out.println(MEMBERSHIP_MESSAGE);
        return readYesNo();
    }

    public boolean readPromotionSuggestion(String productName, Quantity additionalQuantity) {
        System.out.printf(PROMOTION_SUGGESTION_FORMAT,
                productName,
                additionalQuantity.value()
        );
        return readYesNo();
    }

    public boolean readAdditionalPurchase() {
        System.out.println(ADDITIONAL_PURCHASE_MESSAGE);
        return readYesNo();
    }

    private boolean readYesNo() {
        String input = Console.readLine().toUpperCase();
        validateYesNo(input);
        return "Y".equals(input);
    }


    public boolean readInsufficientStockConfirmation(String productName,
                                                     int nonPromotionQuantity) {
        System.out.printf(INSUFFICIENT_STOCK_CONFIRMATION_FORMAT,
                productName,
                nonPromotionQuantity);
        return readYesNo();
    }



}