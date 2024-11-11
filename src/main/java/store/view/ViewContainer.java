package store.view;

import store.util.RetryTemplate;

public class ViewContainer {
    private final InputView inputView;
    private final OutputView outputView;
    private final RetryTemplate retryTemplate;

    public ViewContainer(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.retryTemplate = new RetryTemplate(outputView);
    }

    public InputView getInputView() {
        return inputView;
    }

    public OutputView getOutputView() {
        return outputView;
    }

    public RetryTemplate getRetryTemplate() {
        return retryTemplate;
    }
    public boolean readAdditionalPurchase() {
        return inputView.readAdditionalPurchase();
    }
}
