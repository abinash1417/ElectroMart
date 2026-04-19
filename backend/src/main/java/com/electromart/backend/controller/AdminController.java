package com.electromart.backend.controller;

import com.electromart.backend.dto.CategoryDTO;
import com.electromart.backend.dto.ProductDTO;
import com.electromart.backend.model.*;
import com.electromart.backend.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final ContactService contactService;

    public AdminController(UserService userService,
                           ProductService productService,
                           CategoryService categoryService,
                           PaymentService paymentService,
                           OrderService orderService,
                           ReviewService reviewService,
                           ContactService contactService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.reviewService = reviewService;
        this.contactService=contactService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        List<User> users = userService.getAllUsers();
        List<Order> orders = orderService.getAllOrders();
        List<Payment> payments = paymentService.getAllPayments();
        List<Product> products = productService.getAllProducts();

        double totalRevenue = payments.stream()
                .filter(p -> "SUCCESS".equals(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();

        long pendingOrders = orders.stream()
                .filter(o -> "PENDING".equals(o.getStatus()))
                .count();

        long paidOrders = orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .count();

        stats.put("totalUsers", users.size());
        stats.put("totalOrders", orders.size());
        stats.put("totalProducts", products.size());
        stats.put("totalRevenue", totalRevenue);
        stats.put("pendingOrders", pendingOrders);
        stats.put("paidOrders", paidOrders);

        return stats;
    }

    // User management
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/users/{id}")
    public Map<String, String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return response;
    }

    // Product management
    @PostMapping("/products")
    public Product addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/products/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        return response;
    }

    // category management
    @PostMapping("/categories")
    public Category addCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return categoryService.saveCategory(category);
    }

    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/categories/{id}")
    public Map<String, String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Category deleted successfully");
        return response;
    }

    // Order management
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/orders/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @PutMapping("/orders/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }

    //Payment management
    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/payments/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PutMapping("/payments/{id}/status")
    public Payment updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        return paymentService.updatePaymentStatus(id, status);
    }

    //Review management
    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @DeleteMapping("/reviews/{id}")
    public Map<String, String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Review deleted successfully");
        return response;
    }

    //Contact management
    @GetMapping("/contacts")
    public List<ContactMessage> getAllContactMessages() {
        return contactService.getAllMessages();
    }

    @GetMapping("/contacts/unread")
    public List<ContactMessage> getUnreadMessages() {
        return contactService.getUnreadMessages();
    }

    @PutMapping("/contacts/{id}/read")
    public ContactMessage markAsRead(@PathVariable Long id) {
        return contactService.markAsRead(id);
    }

    @DeleteMapping("/contacts/{id}")
    public Map<String, String> deleteContactMessage(@PathVariable Long id) {
        contactService.deleteMessage(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Contact message deleted successfully");
        return response;
    }
}