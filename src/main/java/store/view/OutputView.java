package store.view;

import java.util.List;
import store.dto.response.ProductResponseDto;
import store.util.ProductFormatter;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";

    public void printWelcome() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printProducts(List<ProductResponseDto> products) {
        products.forEach(this::printProduct);
        System.out.println();
    }

    private void printProduct(ProductResponseDto product) {
        System.out.println(ProductFormatter.formatProductInfo(product));
    }

    public void printExceptionMessage(Exception exception) {
        System.out.println(exception.getMessage());
    }


}
