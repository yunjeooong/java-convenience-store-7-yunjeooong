package store.service;

import store.domain.cart.Cart;
import store.domain.product.Products;
import store.dto.request.CartRequestDto;
import store.util.RetryTemplate;
import store.view.InputView;


public class CartService {
    private final ProductService productService;
    private final InputView inputView;
    private final RetryTemplate retryTemplate;


    private CartService(ProductService productService,InputView inputView,RetryTemplate retryTemplate) {
        this.productService = productService;
        this.inputView = inputView;
        this.retryTemplate = retryTemplate;
    }

    public static CartService create(ProductService productService, InputView inputView, RetryTemplate retryTemplate) {
        return new CartService(productService, inputView, retryTemplate);
    }

    public Cart createCart(CartRequestDto request) {
        Products products = productService.getProducts();
        Cart cart = Cart.create(products);

        request.items().forEach((name, quantity) ->
                cart.addItem(name, quantity));

        return cart;
    }

    public void processPromotions(Cart cart) {
        CartPromotionProcessor promotionProcessor =
                new CartPromotionProcessor(inputView, cart, retryTemplate);
        cart.processPromotions(promotionProcessor);
    }
}
