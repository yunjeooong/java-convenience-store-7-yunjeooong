package store;

import store.config.AppConfig;
import store.controller.MainController;
import store.controller.OrderController;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();  // getInstance() 대신 new 사용
        MainController mainController = appConfig.mainController();
        OrderController orderController = appConfig.orderController();

        try {
            do {
                mainController.start();
                orderController.processOrder();
            } while (orderController.wantsToContinueShopping());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}