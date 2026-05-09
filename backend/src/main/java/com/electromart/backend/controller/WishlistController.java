package com.electromart.backend.controller;

import com.electromart.backend.dto.WishlistDTO;
import com.electromart.backend.model.Product;
import com.electromart.backend.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Product>> getWishlist(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    @GetMapping("/check/{userId}/{productId}")
    public ResponseEntity<Map<String, Boolean>> isInWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        boolean result = wishlistService.isInWishlist(userId, productId);
        return ResponseEntity.ok(Map.of("inWishlist", result));
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, String>> toggleWishlist(@RequestBody WishlistDTO dto) {
        wishlistService.toggleWishlist(dto.getUserId(), dto.getProductId());
        return ResponseEntity.ok(Map.of("message", "Wishlist updated"));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, String>> removeFromWishlist(@RequestBody WishlistDTO dto) {
        wishlistService.removeFromWishlist(dto.getUserId(), dto.getProductId());
        return ResponseEntity.ok(Map.of("message", "Removed from wishlist"));
    }
}