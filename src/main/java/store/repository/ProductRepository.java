package store.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;
import store.domain.stock.Stock;
import store.domain.promotion.PromotionType;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.util.FileReader;

public class ProductRepository {
    private final Products products;
    private final FileReader fileReader;
    private final Map<Product, Stock> stockMap;

    private ProductRepository(FileReader fileReader) {
        this.fileReader = fileReader;
        this.stockMap = new HashMap<>();
        this.products = initializeProducts();
    }

    public static ProductRepository create(FileReader fileReader) {
        return new ProductRepository(fileReader);
    }

    private Products initializeProducts() {
        List<Product> allProducts = createAllProducts();
        initializeStocks(allProducts);
        return Products.from(allProducts);
    }

    private List<Product> createAllProducts() {
        Map<String, PromotionType> promotions = loadPromotions();
        return createProductVersions(promotions);
    }

    private Map<String, PromotionType> loadPromotions() {
        return fileReader.readPromotions().stream()
                .map(this::parsePromotion)
                .collect(Collectors.toMap(PromotionEntry::name, PromotionEntry::type));
    }

    private PromotionEntry parsePromotion(String line) {
        String[] parts = line.split(",");
        validatePromotionParts(parts);
        return new PromotionEntry(parts[0].trim(), PromotionType.from(parts[0].trim()));
    }

    private void validatePromotionParts(String[] parts) {
        if (parts.length != 5) {
            throw new IllegalArgumentException("[ERROR] 프로모션 데이터 형식이 올바르지 않습니다.");
        }
    }

    private List<Product> createProductVersions(Map<String, PromotionType> promotions) {
        Map<String, List<ProductInfo>> groupedProducts = readGroupedProducts();
        return createAllVersions(groupedProducts, promotions);
    }

    private Map<String, List<ProductInfo>> readGroupedProducts() {
        return fileReader.readProducts().stream()
                .map(this::parseProductInfo)
                .collect(Collectors.groupingBy(ProductInfo::name));
    }

    private List<Product> createAllVersions(Map<String, List<ProductInfo>> grouped,
                                            Map<String, PromotionType> promotions) {
        List<Product> allProducts = new ArrayList<>();
        grouped.forEach((name, infos) -> addBothVersions(infos, promotions, allProducts));
        return allProducts;
    }

    private void addBothVersions(List<ProductInfo> infos,
                                 Map<String, PromotionType> promotions,
                                 List<Product> allProducts) {
        addPromotionVersion(infos, promotions, allProducts);
        addNormalVersion(infos, allProducts);
    }

    private void addPromotionVersion(List<ProductInfo> infos,
                                     Map<String, PromotionType> promotions,
                                     Collection<Product> allProducts) {  // List -> Collection
        infos.stream()
                .filter(ProductInfo::hasPromotion)
                .map(info -> createPromotionProduct(info, promotions))
                .forEach(allProducts::add);
    }

    private void addNormalVersion(List<ProductInfo> infos, List<Product> allProducts) {
        ProductInfo regularInfo = findNormalInfoOrCreateEmpty(infos);
        allProducts.add(createNormalProduct(regularInfo));
    }

    private ProductInfo findNormalInfoOrCreateEmpty(List<ProductInfo> infos) {
        return infos.stream()
                .filter(info -> !info.hasPromotion())
                .findFirst()
                .orElse(createEmptyInfo(infos.get(0)));
    }

    private ProductInfo createEmptyInfo(ProductInfo reference) {
        return new ProductInfo(reference.name(), reference.price(), new Quantity(0), "null");
    }

    private ProductInfo parseProductInfo(String line) {
        String[] parts = line.split(",");
        validateProductParts(parts);
        return createProductInfo(parts);
    }

    private void validateProductParts(String[] parts) {
        if (parts.length != 4) {
            throw new IllegalArgumentException("[ERROR] 상품 데이터 형식이 올바르지 않습니다.");
        }
    }

    private ProductInfo createProductInfo(String[] parts) {
        return new ProductInfo(
                parts[0].trim(),
                new Price(Integer.parseInt(parts[1].trim())),
                new Quantity(Integer.parseInt(parts[2].trim())),
                parts[3].trim()
        );
    }

    private void initializeStocks(List<Product> products) {
        products.forEach(this::initializeStock);
    }

    private void initializeStock(Product product) {
        stockMap.put(product, new Stock(product.getStockQuantity()));
    }

    private Product createPromotionProduct(ProductInfo info, Map<String, PromotionType> promotions) {
        PromotionType type = promotions.get(info.promotionName());
        return PromotionProduct.create(
                info.name(),
                info.price(),
                info.quantity(),
                info.quantity(),
                type
        );
    }

    private Product createNormalProduct(ProductInfo info) {
        return Product.create(
                info.name(),
                info.price(),
                info.quantity()
        );
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

    private record ProductInfo(String name, Price price, Quantity quantity, String promotionName) {
        public boolean hasPromotion() {
            return !"null".equals(promotionName);
        }
    }

    private record PromotionEntry(String name, PromotionType type) {
    }
}