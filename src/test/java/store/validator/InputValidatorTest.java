package store.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.vo.Quantity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.validator.InputValidator.ErrorMessages.*;

class InputValidatorTest {

    @Test
    @DisplayName("정상적인 입력을 파싱할 수 있다")
    void validateValidInput() {
        String input = "[사이다-2]";
        List<InputValidator.PurchaseItem> items = InputValidator.validatePurchaseInput(input);

        assertThat(items).hasSize(1);
        assertThat(items.get(0).productName()).isEqualTo("사이다");
        assertThat(items.get(0).quantity()).isEqualTo(new Quantity(2));
    }

    @Test
    @DisplayName("공백이 포함된 정상적인 입력을 파싱할 수 있다")
    void validateInputWithSpaces() {
        String input = "[ 사  이다 -   4]";
        List<InputValidator.PurchaseItem> items = InputValidator.validatePurchaseInput(input);

        assertThat(items).hasSize(1);
        assertThat(items.get(0).productName()).isEqualTo("사  이다");
        assertThat(items.get(0).quantity()).isEqualTo(new Quantity(4));
    }

    @Test
    @DisplayName("여러 상품을 동시에 입력할 수 있다")
    void validateMultipleItems() {
        String input = "[사이다-2],[콜라-3]";
        List<InputValidator.PurchaseItem> items = InputValidator.validatePurchaseInput(input);

        assertThat(items).hasSize(2);
        assertThat(items.get(0).productName()).isEqualTo("사이다");
        assertThat(items.get(0).quantity()).isEqualTo(new Quantity(2));
        assertThat(items.get(1).productName()).isEqualTo("콜라");
        assertThat(items.get(1).quantity()).isEqualTo(new Quantity(3));
    }


    @ParameterizedTest
    @ValueSource(strings = {"[사이다]", "[사이다-]", "[-2]", "사이다-2"})
    @DisplayName("잘못된 형식의 입력에 대해 예외가 발생한다")
    void validateInvalidFormat(String input) {
        assertThatThrownBy(() -> InputValidator.validatePurchaseInput(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_FORMAT.getMessage());
    }

    @Test
    @DisplayName("수량이 0 이하인 경우 예외가 발생한다")
    void validateNonPositiveQuantity() {
        String input = "[사이다-0]";
        assertThatThrownBy(() -> InputValidator.validatePurchaseInput(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_FORMAT.getMessage());
    }

    @Test
    @DisplayName("수량이 숫자가 아닌 경우 예외가 발생한다")
    void validateNonNumericQuantity() {
        String input = "[사이다-abc]";
        assertThatThrownBy(() -> InputValidator.validatePurchaseInput(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_FORMAT.getMessage());
    }

    @Test
    @DisplayName("Y/N 입력 검증 - 정상 입력")
    void validateValidYesNo() {
        InputValidator.validateYesNo("Y");
        InputValidator.validateYesNo("N");
    }

    @ParameterizedTest
    @ValueSource(strings = {"y", "n", "yes", "no", "1", "0", ""})
    @DisplayName("Y/N 입력 검증 - 잘못된 입력")
    void validateInvalidYesNo(String input) {
        assertThatThrownBy(() -> InputValidator.validateYesNo(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_INPUT.getMessage());
    }
}