package store.util;



import org.junit.jupiter.api.Test;
import store.domain.vo.Quantity;
import store.validator.InputValidator;
import store.validator.InputValidator.PurchaseItem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    @Test
    void validatePurchaseInput_정상입력_테스트() {
        String input = "[초코바-2][콜라-5]";
        List<PurchaseItem> items = InputValidator.validatePurchaseInput(input);

        assertEquals(2, items.size(), "아이템 개수는 2개여야 합니다.");
        assertEquals("초코바", items.get(0).productName(), "첫 번째 아이템의 이름이 초코바여야 합니다.");
        assertEquals(new Quantity(2), items.get(0).quantity(), "첫 번째 아이템의 수량이 2여야 합니다.");
        assertEquals("콜라", items.get(1).productName(), "두 번째 아이템의 이름이 콜라여야 합니다.");
        assertEquals(new Quantity(5), items.get(1).quantity(), "두 번째 아이템의 수량이 5여야 합니다.");
    }


    @Test
    void validateNotEmpty_비어있는목록_예외테스트() {
        String input = "[]";
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                InputValidator.validatePurchaseInput(input)
        );
        assertEquals("[ERROR] 최소 1개 이상의 상품을 입력해주세요.", exception.getMessage());
    }

    @Test
    void validateYesNo_Y_정상입력_테스트() {
        assertDoesNotThrow(() -> InputValidator.validateYesNo("Y"));
    }

    @Test
    void validateYesNo_N_정상입력_테스트() {
        assertDoesNotThrow(() -> InputValidator.validateYesNo("N"));
    }

}