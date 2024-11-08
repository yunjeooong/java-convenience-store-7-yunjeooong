package store.controller;

import java.util.List;
import store.dto.response.ProductResponseDto;
import store.service.ProductService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    public final InputView inputView;
    public final OutputView outputView;
    public final ProductService productService;

    public StoreController(InputView inputView,OutputView outputView,ProductService productService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.productService = productService;

    }
    public void displayProducts() {
        outputView.printWelcome();
        List<ProductResponseDto> products = productService.getAllProducts();
        outputView.printProducts(products);
    }

}
