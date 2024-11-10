package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.stock.Stocks;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.dto.response.ProductResponseDto;

public class PromotionProduct extends Product {
    private final PromotionType promotionType;

    public PromotionProduct(String name, Price price, Stocks stocks, PromotionType promotionType) {
        super(name, price, stocks);
        this.promotionType = promotionType;
    }

    public static PromotionProduct create(String name, Price price,
                                          Quantity regularQuantity, Quantity promotionQuantity, PromotionType promotionType) {
        return new PromotionProduct(
                name,
                price,
                Stocks.of(regularQuantity, promotionQuantity),
                promotionType
        );
    }

    public boolean isOnePlusOnePromotion() {
        return promotionType == PromotionType.MD_RECOMMENDED ||
                promotionType == PromotionType.FLASH_SALE;
    }


    @Override
    public boolean isPromotionProduct() {
        return true;
    }

    public boolean canApplyPromotion(Quantity orderQuantity) {
        return promotionType.isApplicable(orderQuantity) &&
                stocks.hasEnoughPromotionStock(orderQuantity);
    }

    public Quantity calculateFreeItems(Quantity orderQuantity) {
        if (!promotionType.isApplicable(orderQuantity)) {
            return Quantity.ZERO;
        }
        return new Quantity(promotionType.calculateFreeItems(orderQuantity));
    }

    public void addPromotionInfoToResponse(ProductResponseDto.Builder builder) {
        builder.withPromotionName(promotionType.getName());
    }
    public String promotionName() {  // getter 형식을 피하고 행위를 표현
        return promotionType.getName();
    }
}