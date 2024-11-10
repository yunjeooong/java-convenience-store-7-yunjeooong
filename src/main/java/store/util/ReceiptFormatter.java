package store.util;

import store.domain.vo.Money;
import java.util.List;
import java.util.stream.Collectors;
import store.dto.response.OrderResponseDto;

public class ReceiptFormatter {

    private ReceiptFormatter() {
    }

    public static String formatReceipt(List<OrderResponseDto.OrderItemDto> orderItems,
                                       List<OrderResponseDto.FreeItemDto> freeItems,
                                       Money totalAmount, Money promotionDiscount, Money membershipDiscount, Money finalAmount) {

        StringBuilder receiptBuilder = new StringBuilder();
        appendHeader(receiptBuilder);
        appendOrderItems(receiptBuilder, orderItems);
        appendFreeItems(receiptBuilder, freeItems);
        appendFooter(receiptBuilder, orderItems, totalAmount, promotionDiscount, membershipDiscount, finalAmount);

        return receiptBuilder.toString();
    }

    private static void appendHeader(StringBuilder receiptBuilder) {
        receiptBuilder.append("==============W 편의점================\n");
        receiptBuilder.append("상품명\t\t수량\t금액\n");
    }

    private static void appendOrderItems(StringBuilder receiptBuilder, List<OrderResponseDto.OrderItemDto> orderItems) {
        String itemsDetails = orderItems.stream()
                .map(ReceiptFormatter::formatOrderLineItem)
                .collect(Collectors.joining("\n"));
        receiptBuilder.append(itemsDetails).append("\n");
    }

    private static void appendFreeItems(StringBuilder receiptBuilder, List<OrderResponseDto.FreeItemDto> freeItems) {
        if (!freeItems.isEmpty()) {
            receiptBuilder.append("=============증\t정===============\n");
            String freeItemsDetails = freeItems.stream()
                    .map(ReceiptFormatter::formatFreeItem)
                    .collect(Collectors.joining("\n"));
            receiptBuilder.append(freeItemsDetails).append("\n");
        }
    }

    private static void appendFooter(StringBuilder receiptBuilder, List<OrderResponseDto.OrderItemDto> orderItems, Money totalAmount, Money promotionDiscount, Money membershipDiscount, Money finalAmount) {
        receiptBuilder.append("====================================\n");
        receiptBuilder.append(String.format("총구매액\t\t%d\t%,d원\n",
                calculateTotalQuantity(orderItems), totalAmount.value()));
        receiptBuilder.append(String.format("행사할인\t\t\t-%,d원\n", promotionDiscount.value()));
        receiptBuilder.append(String.format("멤버십할인\t\t\t-%,d원\n", membershipDiscount.value()));
        receiptBuilder.append(String.format("내실돈\t\t\t %,d원\n", finalAmount.value()));
    }

    private static String formatOrderLineItem(OrderResponseDto.OrderItemDto item) {
        return String.format("%s\t\t%d\t%,d",
                item.name(), item.quantity(), item.price().value());
    }

    private static String formatFreeItem(OrderResponseDto.FreeItemDto item) {
        return String.format("%s\t\t%d", item.name(), item.quantity());
    }

    private static int calculateTotalQuantity(List<OrderResponseDto.OrderItemDto> orderItems) {
        return orderItems.stream()
                .mapToInt(OrderResponseDto.OrderItemDto::quantity)
                .sum();
    }
}