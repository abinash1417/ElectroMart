package com.electromart.backend.service;

import com.electromart.backend.model.Cart;
import com.electromart.backend.model.CartItem;
import com.electromart.backend.model.Product;
import com.electromart.backend.model.User;
import com.electromart.backend.repository.CartItemRepository;
import com.electromart.backend.repository.CartRepository;
import com.electromart.backend.repository.ProductRepository;
import com.electromart.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Cart getCartByUser(Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        return cartRepository.findByUser(user);
    }

    public CartItem addToCart(Long userId, Long productId, int quantity) {

        User user = userRepository.findById(userId).orElse(null);

        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        Product product = productRepository.findById(productId).orElse(null);

        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId).orElse(null);
        if (item != null) {
            Cart cart = item.getCart();
            cart.getCartItems().remove(item);
            cartRepository.save(cart);
            cartItemRepository.deleteById(cartItemId);
        }
    }

    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }
}