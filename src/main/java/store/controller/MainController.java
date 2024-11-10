package store.controller;

import java.util.List;
import store.dto.response.ProductResponseDto;
import store.view.InputView;
import store.view.OutputView;
import store.service.ProductService;
import store.view.ViewContainer;

public class MainController {
    private final ViewContainer viewContainer;
    private final ProductService productService;

    public MainController(ViewContainer viewContainer, ProductService productService) {
        this.viewContainer = viewContainer;
        this.productService = productService;

    }
    public void start() {
        viewContainer.getOutputView().printWelcome();
        displayProducts();
    }

    private void displayProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        viewContainer.getOutputView().printProducts(products);
    }

}
