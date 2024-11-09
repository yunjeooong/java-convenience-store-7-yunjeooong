package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.vo.Quantity;
import store.validator.InputValidator;
import store.validator.InputValidator.PurchaseItem;

public class InputView {
    private static final String PURCHASE_FORMAT_MESSAGE =
            "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP_MESSAGE =
            "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String ADDITIONAL_PURCHASE_MESSAGE =
            "구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String PROMOTION_SUGGESTION_FORMAT =
            "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

    public Map<String, Quantity> readItems() {
        System.out.println(PURCHASE_FORMAT_MESSAGE);
        String input = Console.readLine();
        List<InputValidator.PurchaseItem> purchaseItems =
                InputValidator.validatePurchaseInput(input);

        return convertToMap(purchaseItems);
    }

    private Map<String, Quantity> convertToMap(List<InputValidator.PurchaseItem> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        InputValidator.PurchaseItem::productName,
                        InputValidator.PurchaseItem::quantity
                ));
    }


}