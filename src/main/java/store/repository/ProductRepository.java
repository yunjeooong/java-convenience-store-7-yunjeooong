package store.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;
import store.domain.promotion.PromotionType;
import store.domain.product.Stock;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.util.FileReader;

public class ProductRepository {
    private final Products products;
    private final FileReader fileReader;
    private final Map<Product, Stock> stockMap;

    private ProductRepository(FileReader fileReader) {
        this.fileReader = fileReader;
        this.products = initializeProducts();
        this.stockMap = initializeStockMap();
    }

    public static ProductRepository create(FileReader fileReader) {
        return new ProductRepository(fileReader);
    }

    private Products initializeProducts() {
        Map<String, PromotionType> promotions = loadPromotions();
        List<Product> products = loadProducts(promotions);
        return Products.from(products);
    }

    private Map<String, PromotionType> loadPromotions() {
        return fileReader.readPromotions().stream()
                .map(this::parsePromotion)
                .collect(Collectors.toMap(
                        entry -> entry.name(),
                        entry -> entry.type()
                ));
    }

    private PromotionEntry parsePromotion(String line) {
        String[] parts = line.split(",");
        validatePromotionParts(parts);

        String name = parts[0].trim();
        PromotionType type = PromotionType.from(name);
        return new PromotionEntry(name, type);
    }

    private void validatePromotionParts(String[] parts) {
        if (parts.length != 5) {
            throw new IllegalArgumentException("[ERROR] 프로모션 데이터 형식이 올바르지 않습니다.");
        }
    }

    private List<Product> loadProducts(Map<String, PromotionType> promotions) {
        return fileReader.readProducts().stream()
                .map(line -> createProduct(line, promotions))
                .collect(Collectors.toList());
    }

    private Product createProduct(String line, Map<String, PromotionType> promotions) {
        ProductInfo info = parseProductInfo(line);
        if (!info.hasPromotion()) {
            return createRegularProduct(info);
        }
        return createPromotionProduct(info, promotions);
    }

    private ProductInfo parseProductInfo(String line) {
        String[] parts = line.split(",");
        validateProductParts(parts);

        return new ProductInfo(
                parts[0].trim(),
                new Price(Integer.parseInt(parts[1].trim())),
                new Quantity(Integer.parseInt(parts[2].trim())),
                parts[3].trim()
        );
    }

    private void validateProductParts(String[] parts) {
        if (parts.length != 4) {
            throw new IllegalArgumentException("[ERROR] 상품 데이터 형식이 올바르지 않습니다.");
        }
    }

    private Product createRegularProduct(ProductInfo info) {
        return Product.create(
                info.name(),
                info.price(),
                info.quantity()
        );
    }

    private Product createPromotionProduct(ProductInfo info,
                                           Map<String, PromotionType> promotions) {
        PromotionType type = promotions.get(info.promotionName());
        return PromotionProduct.create(
                info.name(),
                info.price(),
                info.quantity(),
                info.quantity(),
                type
        );
    }

    private Map<Product, Stock> initializeStockMap() {
        return products.getAllProducts().stream()
                .collect(Collectors.toMap(
                        product -> product,
                        product -> new Stock(product.getStockQuantity()) // Stock의 초기 수량 설정
                ));
    }

    public Optional<Stock> getStock(Product product) {
        return Optional.ofNullable(stockMap.get(product));
    }


    public Optional<Product> findByName(String name) {
        return products.findByName(name);
    }

    public List<Product> findAll() {
        return products.getAllProducts();
    }

    private record ProductInfo(
            String name,
            Price price,
            Quantity quantity,
            String promotionName
    ) {
        public boolean hasPromotion() {
            return !"null".equals(promotionName);
        }
    }

    private record PromotionEntry(
            String name,
            PromotionType type
    ) {}
}