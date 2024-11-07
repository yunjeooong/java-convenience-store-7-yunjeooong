package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public enum InputMessage {
        INPUT_PRODUCT_AND_QUANTITY("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1]).");

        private final String message;
        InputMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public String  readProductAndQuantity() {
        System.out.println(InputMessage.INPUT_PRODUCT_AND_QUANTITY.getMessage());
        return Console.readLine();
    }
}