package store.infrastructure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Quantity;

public class StockFileManager {
    private static final String CURRENT_PRODUCTS_FILE = "src/main/resources/current_products.md";
    private static final String INITIAL_PRODUCTS_FILE = "src/main/resources/products.md";
    private static final String PRODUCTS_HEADER = "name,price,quantity,promotion";
    private final boolean isTestMode;

    public StockFileManager(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }

    public List<String> readCurrentProducts() {
        try {
            if (isTestMode) {
                return Files.readAllLines(Paths.get(INITIAL_PRODUCTS_FILE));
            }

            Path currentPath = Paths.get(CURRENT_PRODUCTS_FILE);
            if (Files.exists(currentPath)) {
                return Files.readAllLines(currentPath);
            }
            return Files.readAllLines(Paths.get(INITIAL_PRODUCTS_FILE));
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 상품 정보를 불러올 수 없습니다.");
        }
    }

    public void saveStockState(List<Product> products) {
        if (isTestMode) {
            return;
        }
        List<String> lines = new ArrayList<>();
        lines.add(PRODUCTS_HEADER);
        lines.addAll(convertToLines(products));
        writeStockFile(lines);
    }

    private List<String> convertToLines(List<Product> products) {
        return products.stream()
                .map(this::formatProductLine)
                .collect(Collectors.toList());
    }

    private String formatProductLine(Product product) {
        return String.format("%s,%d,%d,%s",
                product.getName(),
                product.calculateTotalPrice(new Quantity(1)).value(),
                product.getStocks().getStock(product.isPromotionProduct()).getQuantity().value(),
                product.isPromotionProduct() ?
                        ((PromotionProduct)product).promotionName() : "null");
    }

    private void writeStockFile(List<String> lines) {
        try {
            Path stockPath = Paths.get(CURRENT_PRODUCTS_FILE);
            Files.createDirectories(stockPath.getParent());
            Files.write(stockPath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 재고 상태 저장 실패", e);
        }
    }
}