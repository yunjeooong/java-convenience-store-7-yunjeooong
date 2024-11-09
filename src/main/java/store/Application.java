package store;

import store.config.AppConfig;
import store.controller.MainController;
import store.controller.OrderController;


public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        MainController mainController = appConfig.mainController();
        OrderController orderController = appConfig.orderController();

        mainController.start();
        orderController.processOrder();


    }
}