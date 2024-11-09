package store.controller;

import java.util.Map;
import store.domain.vo.Quantity;
import store.dto.response.OrderResponseDto;
import store.service.OrderFacade;
import store.service.ReceiptService;
import store.view.InputView;
import store.view.OutputView;

public class OrderController {
    private final InputView inputView;
    private final OrderFacade orderFacade;
    private final ReceiptService receiptService;
    private final OutputView outputView;

    public OrderController(
            InputView inputView,
            OrderFacade orderFacade,
            ReceiptService receiptService,
            OutputView outputView) {
        this.inputView = inputView;
        this.orderFacade = orderFacade;
        this.receiptService = receiptService;
        this.outputView = outputView;
    }

    public void processOrder() {
        try {
            Map<String, Quantity> items = inputView.readItems();
            boolean hasMembership = inputView.readMembershipChoice();

            OrderResponseDto orderResponse = orderFacade.processOrder(items, hasMembership);
            receiptService.printReceipt(orderResponse);
        } catch (IllegalArgumentException e) {
            outputView.printExceptionMessage(e);
        }
    }
}