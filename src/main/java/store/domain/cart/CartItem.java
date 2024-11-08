package store.domain.cart;

import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class CartItem {
    private final Product product;
    private final Quantity quantity;

    private CartItem(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static CartItem create(Product product, Quantity quantity) {
        return new CartItem(product, quantity);
    }

    public Price calculatePrice() {
        return product.calculateTotalPrice(quantity);
    }

    public void processOrder() {
        product.removeStock(quantity);
    }


    public boolean canApplyPromotion() {
        return product.isPromotionProduct() &&
                ((PromotionProduct) product).canApplyPromotion(quantity);
    }

    public boolean canSuggestAdditionalItems() {
        if (!product.isPromotionProduct()) {
            return false;
        }
        return ((PromotionProduct) product).canSuggestMoreItems(quantity);
    }

    public Quantity calculateAdditionalQuantityNeeded() {
        if (!product.isPromotionProduct()) {
            return Quantity.ZERO;
        }
        return ((PromotionProduct) product).calculateRequiredAdditionalQuantity(quantity);
    }

    public void validateStock() {
        if (!product.hasEnoughStock(quantity)) {
            throw new IllegalArgumentException(
                    String.format("[ERROR] %s의 재고가 부족합니다.", product.getName()));
        }
    }
}