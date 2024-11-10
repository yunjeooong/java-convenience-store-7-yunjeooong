package store.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.promotion.PromotionType;

public class PromotionUtils {

    public static Map<String, PromotionType> loadPromotions(FileReader fileReader) {
        return fileReader.readPromotions().stream()
                .map(PromotionUtils::parsePromotion)
                .collect(Collectors.toMap(PromotionEntry::name, PromotionEntry::type));
    }

    private static PromotionEntry parsePromotion(String line) {
        List<String> parts = List.of(line.split(","));
        validatePromotionParts(parts);
        return new PromotionEntry(parts.get(0).trim(), PromotionType.from(parts.get(0).trim()));
    }

    private static void validatePromotionParts(List<String> parts) {
        if (parts.size() != 5) {
            throw new IllegalArgumentException("[ERROR] 프로모션 데이터 형식이 올바르지 않습니다.");
        }
    }

    private record PromotionEntry(String name, PromotionType type) {
    }
}