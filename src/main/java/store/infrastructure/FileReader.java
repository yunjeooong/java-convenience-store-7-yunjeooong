package store.infrastructure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    private static final String PRODUCTS_FILE = "src/test/resources/products.md";
    private static final String PROMOTIONS_FILE_PATH = "src/test/resources/promotions.md";
    private static final String ERROR_PRODUCTS_LOAD = "[ERROR] 상품 정보를 불러올 수 없습니다.";
    private static final String ERROR_PROMOTIONS_LOAD = "[ERROR] 프로모션 파일을 읽을 수 없습니다.";
    private static final int HEADER_LINE_COUNT = 1;

    private FileReader() {
    }

    public static FileReader create() {
        return new FileReader();
    }

    public List<String> readProducts() {
        try {
            return Files.readAllLines(Paths.get(PRODUCTS_FILE), StandardCharsets.UTF_8)
                    .stream()
                    .skip(HEADER_LINE_COUNT)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_PRODUCTS_LOAD);
        }
    }

    public List<String> readPromotions() {
        try {
            return Files.readAllLines(Paths.get(PROMOTIONS_FILE_PATH), StandardCharsets.UTF_8)
                    .stream()
                    .skip(HEADER_LINE_COUNT)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_PROMOTIONS_LOAD);
        }
    }
}
