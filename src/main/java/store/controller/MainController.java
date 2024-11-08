package store.controller;

import java.util.List;
import store.dto.response.ProductResponseDto;
import store.view.InputView;
import store.view.OutputView;
import store.service.ProductService;

public class MainController {
    private final InputView inputView;
    private final OutputView outputView;
    private final ProductService productService;

    public MainController(InputView inputView, OutputView outputView, ProductService productService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.productService = productService;

    }
    public void start() {
        List<ProductResponseDto> products = productService.getAllProducts();
        outputView.printProducts(products);
    }

}
