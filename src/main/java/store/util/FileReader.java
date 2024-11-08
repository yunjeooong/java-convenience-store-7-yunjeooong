package store.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    private final Path productsPath;
    private final Path promotionsPath;

    private FileReader(String productsPath, String promotionsPath) {
        this.productsPath = Paths.get(productsPath);
        this.promotionsPath = Paths.get(promotionsPath);
    }

    public static FileReader create() {
        return new FileReader(
                "src/main/resources/products.md",
                "src/main/resources/promotions.md"
        );
    }

    public List<String> readProducts() {
        return readFile(productsPath, "상품");
    }

    public List<String> readPromotions() {
        return readFile(promotionsPath, "프로모션");
    }

    private List<String> readFile(Path path, String fileType) {
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8)
                    .stream()
                    .skip(1)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    String.format("[ERROR] %s 파일을 읽을 수 없습니다: %s", fileType, path));
        }
    }
}