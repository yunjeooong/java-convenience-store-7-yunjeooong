package store.config;

import store.controller.MainController;
import store.controller.OrderController;
import store.domain.discount.DiscountManager;
import store.repository.ProductRepository;
import store.service.OrderFacade;
import store.service.OrderService;
import store.service.ProductService;
import store.service.ReceiptService;
import store.util.FileReader;
import store.view.InputView;
import store.view.OutputView;
import store.view.ViewContainer;
import java.util.Collections;

public class AppConfig {
    private AppConfig() {}

    private static class SingleTonHelper {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return SingleTonHelper.INSTANCE;
    }

    public FileReader fileReader() {
        return FileReader.create();
    }

    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }

    public ViewContainer viewContainer() {
        return new ViewContainer(inputView(), outputView());
    }

    public ProductRepository productRepository() {
        return ProductRepository.create(fileReader());
    }

    public ProductService productService() {
        return ProductService.create(productRepository());
    }

    public MainController mainController() {
        return new MainController(viewContainer(), productService());
    }

    public OrderService orderService() {
        return new OrderService(productRepository());
    }

    public ReceiptService receiptService() {
        return new ReceiptService(outputView());
    }

    public OrderFacade orderFacade() {
        return new OrderFacade(orderService(), DiscountManager.create(Collections.emptyList()));
    }

    public OrderController orderController() {
        return new OrderController(
                viewContainer(),
                orderFacade(),
                productService(),
                receiptService()
        );
    }
}