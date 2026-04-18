package com.electromart.backend.controller;

import com.electromart.backend.dto.CartItemDTO;
import com.electromart.backend.model.Cart;
import com.electromart.backend.model.CartItem;
import com.electromart.backend.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/user/{userId}")
    public Cart getCart(@PathVariable Long userId) {

        return cartService.getCartByUser(userId);

    }



    @PostMapping("/add")
    public CartItem addToCart(@RequestBody CartItemDTO cartItemDTO) {

        return cartService.addToCart(
                cartItemDTO.getUserId(),
                cartItemDTO.getProductId(),
                cartItemDTO.getQuantity()
        );

    }



    @DeleteMapping("/remove/{cartItemId}")
    public String removeItem(@PathVariable Long cartItemId) {

        cartService.removeCartItem(cartItemId);

        return "Item Removed";

    }



    @DeleteMapping("/clear/{cartId}")
    public String clearCart(@PathVariable Long cartId) {

        cartService.clearCart(cartId);

        return "Cart Cleared";

    }

}