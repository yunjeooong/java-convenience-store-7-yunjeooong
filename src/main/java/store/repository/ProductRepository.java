package store.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.RegularProduct;
import store.domain.product.PromotionProduct;
import store.domain.promotion.PromotionType;
import store.domain.vo.Quantity;
import store.infrastructure.FileReader;
import store.infrastructure.StockFileManager;
import store.util.PromotionUtils;
import store.util.ProductInfoParser;
import store.util.ProductInfoParser.ProductInfo;

public class ProductRepository {
    private Products products;
    private final FileReader fileReader;
    private final StockFileManager stockFileManager;

    private ProductRepository(FileReader fileReader, StockFileManager stockFileManager) {
        this.fileReader = fileReader;
        this.stockFileManager = stockFileManager;
        this.products = initializeProducts();
    }

    public static ProductRepository create(FileReader fileReader, StockFileManager stockFileManager) {
        return new ProductRepository(fileReader, stockFileManager);
    }

    private Products initializeProducts() {
        Map<String, PromotionType> promotions = PromotionUtils.loadPromotions(fileReader);
        List<Product> allProducts = createAllProducts(promotions);
        return Products.from(allProducts);
    }

    private List<Product> createAllProducts(Map<String, PromotionType> promotions) {
        Map<String, List<ProductInfo>> groupedProducts = readGroupedProducts();
        return createAllVersions(groupedProducts, promotions);
    }

    private Map<String, List<ProductInfo>> readGroupedProducts() {
        return fileReader.readProducts().stream()
                .map(ProductInfoParser::parseProductInfo)
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
                                     Collection<Product> allProducts) {
        infos.stream()
                .filter(ProductInfo::hasPromotion)
                .map(info -> createPromotionProduct(info, promotions))
                .forEach(allProducts::add);
    }

    private void addNormalVersion(List<ProductInfo> infos, List<Product> allProducts) {
        ProductInfo regularInfo = findNormalInfoOrCreateEmpty(infos);
        allProducts.add(createNormalProduct(regularInfo));
    }

    public void refreshProducts() {
        this.products = initializeProducts();
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
        return RegularProduct.create(
                info.name(),
                info.price(),
                info.quantity(),
                new Quantity(0)
        );
    }

    public Optional<Product> findByName(String name) {
        return products.findByName(name);
    }

    public List<Product> findAll() {
        return products.getAllProducts();
    }

    public void saveCurrentState() {
        stockFileManager.saveStockState(findAll());
    }
}