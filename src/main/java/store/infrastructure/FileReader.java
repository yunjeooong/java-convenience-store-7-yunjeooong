package store.infrastructure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    private static final String PRODUCTS_FILE = "src/main/resources/products.md";
    private static final String ERROR_PRODUCT_READ = "[ERROR] 상품 정보를 불러올 수 없습니다.";
    private static final String ERROR_PROMOTION_READ = "[ERROR] 프로모션 파일을 읽을 수 없습니다.";
    private static final int SKIP_HEADER_LINES = 1;

    private FileReader() {
    }

    public static FileReader create() {
        return new FileReader();
    }

    public List<String> readProducts() {
        try {
            return Files.readAllLines(Paths.get(PRODUCTS_FILE), StandardCharsets.UTF_8)
                    .stream()
                    .skip(SKIP_HEADER_LINES)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_PRODUCT_READ);
        }
    }

    public List<String> readPromotions() {
        try {
            return Files.readAllLines(Paths.get(FileConstants.PROMOTIONS_FILE_PATH))
                    .stream()
                    .skip(SKIP_HEADER_LINES)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_PROMOTION_READ);
        }
    }
}