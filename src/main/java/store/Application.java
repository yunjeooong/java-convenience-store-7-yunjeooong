package store;

import store.config.AppConfig;
import store.controller.MainController;
import store.controller.OrderController;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();  // 싱글톤 인스턴스 사용
        MainController mainController = appConfig.mainController();
        OrderController orderController = appConfig.orderController();

        try {
            mainController.start();
            orderController.processOrder();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}