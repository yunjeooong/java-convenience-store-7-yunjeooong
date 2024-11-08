package store;

import store.util.FileReader;

public class Application {
    public static void main(String[] args) {
        FileReader fileReader = new FileReader();

        System.out.println("제품 목록:");
        fileReader.readProducts().forEach(line -> System.out.println(formatProduct(line)));

        System.out.println("\n프로모션 목록:");
        fileReader.readPromotions().forEach(line -> System.out.println(line));
    }

    private static String formatProduct(String productData) {
        String[] parts = productData.split(",");
        String name = parts[0];
        String price = parts[1];
        String quantity = parts[2];
        String promotion = "";

        if (parts.length > 3) {
            promotion = parts[3];
        }

        return String.format("- %s %s원 %s개 %s", name, price, quantity, promotion).trim();
    }
}