package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.stock.Stock;
import store.domain.vo.Quantity;

public class PromotionProduct {
    private final Product product;
    private final Stock stock;
    private final PromotionType promotionType;

    public PromotionProduct(Product product, Stock stock, PromotionType promotionType) {
        this.product = product;
        this.stock = stock;
        this.promotionType = promotionType;
    }

    public String getName() {
        return product.getName();
    }

    public int getPrice() {
        return product.getAmount();
    }

    public boolean canApplyPromotion() {
        Quantity currentQuantity = new Quantity(stock.getQuantity());
        return promotionType.isApplicable(currentQuantity) && 
               stock.hasEnough(promotionType.calculateFreeItems(currentQuantity));
    }

    public int calculateFreeItems() {
        Quantity currentQuantity = new Quantity(stock.getQuantity());
        if (!promotionType.isApplicable(currentQuantity)) {
            return 0;
        }
        return promotionType.calculateFreeItems(currentQuantity);
    }

    public void decreaseStock(int quantity) {
        stock.decrease(quantity);
    }

    public void decreasePromotionStock() {
        if (canApplyPromotion()) {
            stock.decrease(calculateFreeItems());
        }
    }

    public boolean hasEnoughStock(int quantity) {
        return stock.hasEnough(quantity);
    }

    public int getStock() {
        return stock.getQuantity();
    }
} 