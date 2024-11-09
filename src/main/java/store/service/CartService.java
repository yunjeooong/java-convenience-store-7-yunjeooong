package store.service;

import store.domain.cart.Cart;
import store.domain.product.Products;
import store.dto.request.CartRequestDto;

public class CartService {
    private final ProductService productService;

    private CartService(ProductService productService) {
        this.productService = productService;
    }

    public static CartService create(ProductService productService) {
        return new CartService(productService);
    }

    public Cart createCart(CartRequestDto request) {
        Products products = productService.getProducts();
        Cart cart = Cart.create(products);

        request.items().forEach((name, quantity) ->
                cart.addItem(name, quantity));

        return cart;
    }
}
