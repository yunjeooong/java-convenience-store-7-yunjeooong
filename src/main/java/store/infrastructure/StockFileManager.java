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
    public void saveStockState(List<Product> products) {
        List<String> lines = new ArrayList<>();
        lines.add(FileConstants.PRODUCTS_HEADER);
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
                formatPromotion(product));
    }

    private String formatPromotion(Product product) {
        if (!product.isPromotionProduct()) {
            return "null";
        }
        return ((PromotionProduct)product).promotionName();
    }

    private void writeStockFile(List<String> lines) {
        try {
            Path stockPath = Paths.get(FileConstants.CURRENT_PRODUCTS_FILE_PATH);
            Files.createDirectories(stockPath.getParent());
            Files.write(stockPath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 재고 상태 저장 실패", e);
        }
    }

    public List<String> readCurrentProducts() {
        try {
            if (Files.exists(Paths.get(FileConstants.CURRENT_PRODUCTS_FILE_PATH))) {
                return Files.readAllLines(Paths.get(FileConstants.CURRENT_PRODUCTS_FILE_PATH));
            }
            // 재고 파일이 없으면 초기 products.md 반환
            return Files.readAllLines(Paths.get(FileConstants.PRODUCTS_FILE_PATH));
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 상품 정보를 불러올 수 없습니다.", e);
        }
    }
}