package store.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.promotion.PromotionType;
import store.infrastructure.FileReader;

public class PromotionUtils {

    private PromotionUtils() {
    }

    public static Map<String, PromotionType> loadPromotions(FileReader fileReader) {
        return fileReader.readPromotions().stream()
                .map(PromotionUtils::parsePromotion)
                .collect(Collectors.toMap(PromotionEntry::name, PromotionEntry::type));
    }

    private static PromotionEntry parsePromotion(String line) {
        String[] parts = line.split(",");
        validatePromotionParts(parts);
        String name = parts[0].trim();
        return new PromotionEntry(name, PromotionType.from(name));
    }

    private static void validatePromotionParts(String[] parts) {
        if (parts.length != 5) {
            throw new IllegalArgumentException("[ERROR] 프로모션 데이터 형식이 올바르지 않습니다.");
        }
    }

    private record PromotionEntry(String name, PromotionType type) {
    }
}