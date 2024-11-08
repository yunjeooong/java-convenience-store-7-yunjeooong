package store.config;

import store.controller.MainController;
import store.repository.ProductRepository;
import store.service.ProductService;
import store.util.FileReader;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {
    private AppConfig() {}

    private static class SingleTonHelper {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return SingleTonHelper.INSTANCE;
    }

    public FileReader fileReader() {
        return FileReader.create();
    }

    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }

    public ProductRepository productRepository() {
        return ProductRepository.create(fileReader());
    }

    public ProductService productService() {
        return ProductService.create(productRepository());
    }

    public MainController mainController() {
        return new MainController(inputView(), outputView(), productService());
    }
}