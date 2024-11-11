package store.config;

import store.controller.MainController;
import store.controller.OrderController;
import store.domain.discount.DiscountManager;
import store.domain.discount.DiscountPolicy;
import store.domain.discount.MembershipDiscountPolicy;
import store.domain.discount.PromotionDiscountPolicy;
import store.infrastructure.FileReader;
import store.repository.ProductRepository;
import store.service.OrderFacade;
import store.service.OrderService;
import store.service.ProductService;
import store.service.ReceiptService;
import store.view.InputView;
import store.view.OutputView;
import store.view.ViewContainer;
import java.util.Arrays;
import java.util.List;

public class AppConfig {

    public AppConfig() {
    }

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

    public OrderService orderService() {
        return new OrderService(productRepository());
    }

    public DiscountManager discountManager() {
        List<DiscountPolicy> policies = Arrays.asList(
                new PromotionDiscountPolicy(),
                new MembershipDiscountPolicy()
        );
        return DiscountManager.create(policies);
    }

    public OrderFacade orderFacade() {
        return new OrderFacade(
                orderService(),
                discountManager(),
                productRepository()
        );
    }

    public ReceiptService receiptService() {
        return new ReceiptService(outputView());
    }

    public OrderController orderController() {
        return new OrderController(
                viewContainer(),
                orderFacade(),
                productService(),
                receiptService()
        );
    }

    public MainController mainController() {
        return new MainController(viewContainer(), productService());
    }
}