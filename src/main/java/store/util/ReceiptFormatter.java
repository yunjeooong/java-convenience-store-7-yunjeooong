package store.util;

import store.domain.order.OrderLineItem;
import store.domain.vo.Money;

import java.util.List;
import java.util.stream.Collectors;

public class ReceiptFormatter {

    private ReceiptFormatter() {
    }

    public static String formatReceipt(List<OrderLineItem> orderItems, Money totalAmount, Money discountAmount, Money finalAmount) {
        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append("==============W 편의점================\n");

        String itemsDetails = orderItems.stream()
                .map(ReceiptFormatter::formatOrderLineItem)
                .collect(Collectors.joining("\n"));
        receiptBuilder.append(itemsDetails).append("\n");

        receiptBuilder.append("====================================\n");
        receiptBuilder.append(String.format("총구매액\t\t%,d원\n", totalAmount.value()));
        receiptBuilder.append(String.format("행사할인\t\t-%,d원\n", discountAmount.value()));
        receiptBuilder.append(String.format("내실돈\t\t%,d원\n", finalAmount.value()));

        return receiptBuilder.toString();
    }

    private static String formatOrderLineItem(OrderLineItem item) {
        return String.format("%s\t\t%d\t%,d원", item.productName(), item.productName(), item.quantityValue(), item.calculateItemPrice().value());
    }
}
