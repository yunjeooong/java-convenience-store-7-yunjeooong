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
    private final ProductRepository productRepository;
    private final FileReader fileReader;
    private final InputView inputView;
    private final OutputView outputView;
    private final ViewContainer viewContainer;
    private final ProductService productService;
    private final OrderService orderService;
    private final DiscountManager discountManager;
    private final OrderFacade orderFacade;
    private final ReceiptService receiptService;

    public AppConfig() {  // 생성자를 public으로 변경
        this.fileReader = FileReader.create();
        this.productRepository = ProductRepository.create(fileReader);
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.viewContainer = new ViewContainer(inputView, outputView);
        this.productService = ProductService.create(productRepository);
        this.orderService = new OrderService(productRepository);
        this.discountManager = createDiscountManager();
        this.orderFacade = new OrderFacade(orderService, discountManager, productRepository);
        this.receiptService = new ReceiptService(outputView);
    }

    private DiscountManager createDiscountManager() {
        List<DiscountPolicy> policies = Arrays.asList(
                new PromotionDiscountPolicy(),
                new MembershipDiscountPolicy()
        );
        return DiscountManager.create(policies);
    }

    // getter 메서드들은 그대로 유지
    public FileReader fileReader() {
        return this.fileReader;
    }

    public InputView inputView() {
        return this.inputView;
    }

    public OutputView outputView() {
        return this.outputView;
    }

    public ViewContainer viewContainer() {
        return this.viewContainer;
    }

    public ProductRepository productRepository() {
        return this.productRepository;
    }

    public ProductService productService() {
        return this.productService;
    }

    public OrderService orderService() {
        return this.orderService;
    }

    public DiscountManager discountManager() {
        return this.discountManager;
    }

    public OrderFacade orderFacade() {
        return this.orderFacade;
    }

    public ReceiptService receiptService() {
        return this.receiptService;
    }

    public OrderController orderController() {
        return new OrderController(
                viewContainer,
                orderFacade,
                productService,
                receiptService
        );
    }

    public MainController mainController() {
        return new MainController(viewContainer, productService);
    }
}