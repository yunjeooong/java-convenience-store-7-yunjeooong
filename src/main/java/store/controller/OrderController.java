package store.controller;

import java.util.List;
import java.util.Map;
import store.domain.vo.Quantity;
import store.dto.request.OrderRequestDto;
import store.dto.response.OrderResponseDto;
import store.service.OrderFacade;
import store.service.ReceiptService;
import store.view.InputView;
import store.view.OutputView;
import store.view.ViewContainer;

public class OrderController {
    private final ViewContainer viewContainer;
    private final OrderFacade orderFacade;
    private final ReceiptService receiptService;

    public OrderController(
            ViewContainer viewContainer,
            OrderFacade orderFacade,
            ReceiptService receiptService) {
        this.viewContainer = viewContainer;
        this.orderFacade = orderFacade;
        this.receiptService = receiptService;
    }

    public void processOrder() {
        try {
            OrderResponseDto orderResponse = createOrder();
            receiptService.printReceipt(orderResponse);
        } catch (IllegalArgumentException e) {
            viewContainer.getOutputView().printExceptionMessage(e);
        }
    }

    private OrderResponseDto createOrder() {
        return viewContainer.getRetryTemplate().execute(() -> {
            List<OrderRequestDto> items = viewContainer.getInputView().readItems();
            boolean hasMembership = viewContainer.getInputView().readMembershipChoice();
            return orderFacade.processOrder(items, hasMembership);
        });
    }
}