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
            List<OrderRequestDto> finalRequests = processPromotionRequests(orderRequests);
            boolean hasMembership = viewContainer.getInputView().readMembershipChoice();

            OrderResponseDto orderResponse = orderFacade.processOrder(finalRequests, hasMembership);
            receiptService.printReceipt(orderResponse);
        });
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
        return handlePromotionProduct(product, request);
    }

    private OrderRequestDto handlePromotionProduct(Product product, OrderRequestDto request) {
        PromotionProduct promotionProduct = (PromotionProduct) product;
        if (!canApplyPromotion(promotionProduct, request.quantity())) {
            return request;
        }
        return createPromotionOrderRequest(promotionProduct, request);
    }

    private boolean canApplyPromotion(PromotionProduct product, Quantity quantity) {
        return product.canApplyPromotion(quantity);
    }

    private OrderRequestDto createPromotionOrderRequest(PromotionProduct product,
                                                        OrderRequestDto request) {
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

    private OrderRequestDto createRequestWithFreeItems(OrderRequestDto request,
                                                       Quantity freeQuantity) {
        return new OrderRequestDto(
                request.productName(),
                request.quantity().add(freeQuantity)
        );
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