package store.service;

import store.domain.order.Order;
import store.domain.vo.Money;
import store.util.ReceiptFormatter;
import store.view.OutputView;

public class ReceiptService {
    private final OutputView outputView;

    public ReceiptService(OutputView outputView) {
        this.outputView = outputView;
    }

    public void printReceipt(Order order, Money discountAmount) {
        Money totalAmount = order.calculateTotalAmount();
        Money finalAmount = totalAmount.subtract(discountAmount);

        String formattedReceipt = ReceiptFormatter.formatReceipt(order.getOrderItems(), totalAmount, discountAmount, finalAmount);
        outputView.printReceiptMessage(formattedReceipt);
    }
}
