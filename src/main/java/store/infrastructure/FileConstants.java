package store.infrastructure;

public class FileConstants {
    private FileConstants() {
    }

    public static final String RESOURCES_PATH = "src/main/resources";
    public static final String PRODUCTS_FILE_PATH = RESOURCES_PATH + "/products.md";
    public static final String PROMOTIONS_FILE_PATH = RESOURCES_PATH + "/promotions.md";
    public static final String CURRENT_PRODUCTS_FILE_PATH = RESOURCES_PATH + "/current_products.md";

    public static final String PRODUCTS_HEADER = "name,price,quantity,promotion";
    public static final String PROMOTIONS_HEADER = "name,buy,get,start_date,end_date";
}