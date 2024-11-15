package store.service;

import java.util.List;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.stock.Stock;
import store.dto.response.ProductResponseDto;
import store.repository.ProductRepository;

public class ProductService {
    private final ProductRepository productRepository;

    private ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public static ProductService create(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toProductResponseDto)
                //.filter(this::hasAvailableStock)
                .collect(Collectors.toList());
    }
    private ProductResponseDto toProductResponseDto(Product product) {
        Stock stock = getProductStock(product);
        return ProductResponseDto.from(product, stock.getQuantity());
    }
    private Stock getProductStock(Product product) {
        return product.getStocks()
                .getStock(product.isPromotionProduct());
    }
    private boolean hasAvailableStock(ProductResponseDto product) {
        return product.stockStatus() > 0;
    }
    public Products getProducts() {
        List<Product> products = productRepository.findAll();
        return Products.from(products);
    }
    public Product findProduct(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("[ERROR] 존재하지 않는 상품입니다: %s", name)));
    }
}