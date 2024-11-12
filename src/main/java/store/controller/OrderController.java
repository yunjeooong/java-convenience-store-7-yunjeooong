package store.controller;

import java.util.List;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Quantity;
import store.dto.request.OrderRequestDto;
import store.dto.response.OrderResponseDto;
import store.service.OrderFacade;
import store.service.ProductService;
import store.service.ReceiptService;
import store.view.ViewContainer;

public class OrderController {
    private final ViewContainer viewContainer;
    private final OrderFacade orderFacade;
    private final ProductService productService;
    private final ReceiptService receiptService;

    public OrderController(ViewContainer viewContainer,
                           OrderFacade orderFacade,
                           ProductService productService,
                           ReceiptService receiptService) {
        this.viewContainer = viewContainer;
        this.orderFacade = orderFacade;
        this.productService = productService;
        this.receiptService = receiptService;
    }

    public void processOrder() {
        do {
            processSingleOrder();
        } while (checkAdditionalPurchase());
    }

    private void processSingleOrder() {
        viewContainer.getRetryTemplate().execute(() -> {
            List<OrderRequestDto> orderRequests = viewContainer.getInputView().readItems();
            validateOrderQuantities(orderRequests);
            processValidOrder(orderRequests);
        });
    }

    private void processValidOrder(List<OrderRequestDto> orderRequests) {
        List<OrderRequestDto> finalRequests = processPromotionRequests(orderRequests);
        boolean hasMembership = viewContainer.getInputView().readMembershipChoice();
        OrderResponseDto orderResponse = orderFacade.processOrder(finalRequests, hasMembership);
        receiptService.printReceipt(orderResponse);
    }

    private void validateOrderQuantities(List<OrderRequestDto> orderRequests) {
        orderRequests.forEach(this::validateSingleOrder);
    }

    private void validateSingleOrder(OrderRequestDto request) {
        Product product = productService.findProduct(request.productName());
        validateOrderQuantity(product, request.quantity());
    }

    private void validateOrderQuantity(Product product, Quantity quantity) {
        product.validateStock(calculateTotalQuantity(product, quantity));
    }

    private Quantity calculateTotalQuantity(Product product, Quantity quantity) {
        if (product.isPromotionProduct()) {
            return calculatePromotionQuantity((PromotionProduct) product, quantity);
        }
        return quantity;
    }

    private Quantity calculatePromotionQuantity(PromotionProduct product, Quantity quantity) {
        return quantity.add(product.getPromotionType().calculateFreeItems(quantity));
    }

    private List<OrderRequestDto> processPromotionRequests(List<OrderRequestDto> orderRequests) {
        return orderRequests.stream()
                .map(this::processPromotionRequest)
                .collect(Collectors.toList());
    }

    private OrderRequestDto processPromotionRequest(OrderRequestDto request) {
        Product product = productService.findProduct(request.productName());
        return createPromotionRequest(product, request);
    }

    private OrderRequestDto createPromotionRequest(Product product, OrderRequestDto request) {
        if (!product.isPromotionProduct()) {
            return request;
        }
        return handlePromotionProduct((PromotionProduct) product, request);
    }

    private OrderRequestDto handlePromotionProduct(PromotionProduct product, OrderRequestDto request) {
        if (!canApplyPromotion(product, request.quantity())) {
            return request;
        }
        return createPromotionOrderRequest(product, request);
    }

    private boolean canApplyPromotion(PromotionProduct product, Quantity quantity) {
        return product.canApplyPromotion(quantity);
    }

    private OrderRequestDto createPromotionOrderRequest(PromotionProduct product, OrderRequestDto request) {
        Quantity freeQuantity = calculateFreeItems(product, request.quantity());
        if (!shouldAddFreeItems(product.getName(), freeQuantity)) {
            return request;
        }
        return createRequestWithFreeItems(request, freeQuantity);
    }

    private Quantity calculateFreeItems(PromotionProduct product, Quantity quantity) {
        return new Quantity(product.calculateFreeItems(quantity).value());
    }

    private boolean shouldAddFreeItems(String productName, Quantity freeQuantity) {
        return viewContainer.getInputView().readPromotionSuggestion(productName, freeQuantity);
    }

    private OrderRequestDto createRequestWithFreeItems(OrderRequestDto request, Quantity freeQuantity) {
        return new OrderRequestDto(request.productName(), request.quantity().add(freeQuantity));
    }

    private boolean checkAdditionalPurchase() {
        boolean wantsToContinue = viewContainer.getInputView().readAdditionalPurchase();
        if (wantsToContinue) {
            viewContainer.getOutputView().printProducts(productService.getAllProducts());
        }
        return wantsToContinue;
    }

    public boolean wantsToContinueShopping() {
        return viewContainer.readAdditionalPurchase();
    }
}