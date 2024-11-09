package store.dto.response;

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
                toOrderItemDtos(order.getOrderItems()),
                toFreeItemDtos(order.getFreeItems()),
                order.calculateTotalAmount(),
                order.getPromotionDiscount(),
                order.getMembershipDiscount(),
                order.getFinalAmount()
        );
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
                    item.quantityValue()
            );
        }
    }
}