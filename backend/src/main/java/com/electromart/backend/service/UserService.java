package com.electromart.backend.service;

import com.electromart.backend.dto.AuthResponse;
import com.electromart.backend.dto.UserDTO;
import com.electromart.backend.exception.BadRequestException;
import com.electromart.backend.exception.ResourceNotFoundException;
import com.electromart.backend.model.Cart;
import com.electromart.backend.model.Role;
import com.electromart.backend.model.User;
import com.electromart.backend.repository.RoleRepository;
import com.electromart.backend.repository.CartRepository;
import com.electromart.backend.repository.OrderRepository;
import com.electromart.backend.repository.ReviewRepository;
import com.electromart.backend.repository.WishlistRepository;
import com.electromart.backend.repository.UserRepository;
import com.electromart.backend.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager,
                       CartRepository cartRepository,
                       WishlistRepository wishlistRepository,
                       OrderRepository orderRepository,
                       ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.cartRepository = cartRepository;
        this.wishlistRepository = wishlistRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    public User registerUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new BadRequestException(
                    "Email already registered: " + userDTO.getEmail());
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role ROLE_USER not found — seed the roles table first"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .city(userDTO.getCity())
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    public AuthResponse loginUser(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateToken(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", 0L));

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthResponse(token, user.getId(), user.getName(), email, roles);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        reviewRepository.findByUserId(id).forEach(reviewRepository::delete);

        wishlistRepository.findByUserId(id).forEach(wishlistRepository::delete);

        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            cartRepository.delete(cart);
        }

        orderRepository.findByUserId(id).forEach(orderRepository::delete);

        user.getRoles().clear();
        userRepository.save(user);

        userRepository.deleteById(id);
    }
}