package store.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    private final StockFileManager stockFileManager;

    private FileReader(StockFileManager stockFileManager) {
        this.stockFileManager = stockFileManager;
    }

    public static FileReader create(StockFileManager stockFileManager) {
        return new FileReader(stockFileManager);
    }

    public List<String> readProducts() {
        return stockFileManager.readCurrentProducts().stream()
                .skip(1)
                .collect(Collectors.toList());
    }

    public List<String> readPromotions() {
        try {
            return Files.readAllLines(Paths.get(FileConstants.PROMOTIONS_FILE_PATH))
                    .stream()
                    .skip(1)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 프로모션 파일을 읽을 수 없습니다.");
        }
    }
}