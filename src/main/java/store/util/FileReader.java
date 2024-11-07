package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    private static final String PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_PATH = "src/main/resources/promotions.md";

    public List<String> readProducts() {
        return readFile(PRODUCTS_PATH);
    }

    public List<String> readPromotions() {
        return readFile(PROMOTIONS_PATH);
    }

    List<String> readFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath))
                    .stream()
                    .skip(1)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 파일을 읽을 수 없습니다.");
        }
    }

}
