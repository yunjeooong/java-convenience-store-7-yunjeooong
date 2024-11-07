package store.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {
    private FileReader fileReader;
    private static final String TEST_PRODUCTS_PATH = "src/test/resources/test-products.md";
    private static final String TEST_PROMOTIONS_PATH = "src/test/resources/test-promotions.md";

    @BeforeEach
    void setUp() {
        fileReader = new FileReader();
    }

    @Test
    void testReadProducts() {
        List<String> products = fileReader.readProducts();

        products.forEach(System.out::println);

        assertNotNull(products);
        assertEquals(16, products.size());
        assertTrue(products.get(0).contains("콜라"));
        assertTrue(products.get(2).contains("사이다"));
    }

    @Test
    void testReadPromotions() {
        List<String> promotions = fileReader.readPromotions();

        assertNotNull(promotions);
        assertEquals(3, promotions.size());
        assertTrue(promotions.get(0).contains("탄산2+1"));
        assertTrue(promotions.get(1).contains("MD추천상품"));
    }

    @Test
    void testFileNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fileReader.readFile("nonexistent-file.md");
        });
        assertEquals("[ERROR] 파일을 읽을 수 없습니다.", exception.getMessage());
    }
}