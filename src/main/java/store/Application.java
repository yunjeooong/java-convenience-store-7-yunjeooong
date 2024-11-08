package store;

import store.config.AppConfig;
import store.controller.MainController;
import store.controller.StoreController;
import store.view.InputView;


public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        MainController mainController = appConfig.mainController();
        mainController.start();


    }
}