package com.electromart.backend.repository;

import com.electromart.backend.model.Cart;
import com.electromart.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUser(User user);

}