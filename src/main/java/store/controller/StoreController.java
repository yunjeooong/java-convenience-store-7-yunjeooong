package store.controller;

import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    public final InputView inputView;
    public final OutputView outputView;

    public StoreController(InputView inputView,OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

}
