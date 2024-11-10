package store.dto.response;

import java.util.stream.Collectors;
import store.domain.order.Order;
import store.domain.order.OrderLineItem;
import store.domain.vo.Money;
import java.util.List;

public record OrderResponseDto(
        List<OrderItemDto> orderItems,
        List<FreeItemDto> freeItems,
        Money totalAmount,
        Money promotionDiscount,
        Money membershipDiscount,
        Money finalAmount
) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                createOrderItems(order),
                createFreeItems(order),
                calculateTotalAmount(order),
                getPromotionDiscount(order),
                getMembershipDiscount(order),
                calculateFinalAmount(order)
        );
    }

    private static List<OrderItemDto> createOrderItems(Order order) {
        return order.getOrderItems().stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());
    }

    private static List<FreeItemDto> createFreeItems(Order order) {
        return order.getFreeItems().stream()
                .map(FreeItemDto::from)
                .collect(Collectors.toList());
    }

    private static Money calculateTotalAmount(Order order) {
        return order.calculateTotalAmount();
    }

    private static Money getPromotionDiscount(Order order) {
        return order.getPromotionDiscount();
    }

    private static Money getMembershipDiscount(Order order) {
        return order.getMembershipDiscount();
    }

    private static Money calculateFinalAmount(Order order) {
        return order.getFinalAmount();
    }

    public record OrderItemDto(String name, int quantity, Money price) {
        public static OrderItemDto from(OrderLineItem item) {
            return new OrderItemDto(
                    item.productName(),
                    item.quantityValue(),
                    item.calculateItemPrice()
            );
        }
    }

    public record FreeItemDto(String name, int quantity) {
        public static FreeItemDto from(OrderLineItem item) {
            return new FreeItemDto(
                    item.productName(),
                    item.calculateFreeQuantity().value()
            );
        }
    }
}