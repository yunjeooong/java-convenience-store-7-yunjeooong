package store.service;

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
                orderResponse.freeItems(),
                orderResponse.totalAmount(),
                orderResponse.promotionDiscount(),
                orderResponse.membershipDiscount(),
                orderResponse.finalAmount()
        );
        outputView.printReceiptMessage(formattedReceipt);
    }
}