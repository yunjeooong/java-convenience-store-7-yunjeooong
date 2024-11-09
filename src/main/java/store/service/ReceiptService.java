package store.service;

import store.domain.order.Order;
import store.domain.vo.Money;
import store.dto.response.OrderResponseDto;
import store.util.ReceiptFormatter;
import store.view.OutputView;

public class ReceiptService {
    private final OutputView outputView;

    public ReceiptService(OutputView outputView) {
        this.outputView = outputView;
    }

    public void printReceipt(OrderResponseDto orderResponse) {
        String formattedReceipt = ReceiptFormatter.formatReceipt(
                orderResponse.orderItems(),
                orderResponse.totalAmount(),
                orderResponse.promotionDiscount(),
                orderResponse.finalAmount()
        );
        outputView.printReceiptMessage(formattedReceipt);
    }
}
