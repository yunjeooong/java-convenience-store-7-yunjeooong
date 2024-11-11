package store.config;

import store.controller.MainController;
import store.controller.OrderController;
import store.domain.discount.DiscountManager;
import store.domain.discount.DiscountPolicy;
import store.domain.discount.MembershipDiscountPolicy;
import store.domain.discount.PromotionDiscountPolicy;
import store.infrastructure.FileReader;
import store.infrastructure.StockFileManager;
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
    private final boolean isTestMode;

    // 테스트용 생성자
    public AppConfig(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }

    // 실제 운영용 싱글톤 인스턴스
    private static class SingleTonHelper {
        private static final AppConfig INSTANCE = new AppConfig(false);
    }

    public static AppConfig getInstance() {
        return SingleTonHelper.INSTANCE;
    }

    public StockFileManager stockFileManager() {
        return new StockFileManager(isTestMode);
    }

    public FileReader fileReader() {
        return FileReader.create(stockFileManager());
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
        return ProductRepository.create(fileReader(), stockFileManager());
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