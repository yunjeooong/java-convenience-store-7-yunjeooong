package store;

import store.config.AppConfig;
import store.controller.MainController;


public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        MainController mainController = appConfig.mainController();

        mainController.start();

    }
}