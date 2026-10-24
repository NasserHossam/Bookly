package com.bookstore.demo.service.impl;

import com.bookstore.demo.dto.OrderItemResponse;
import com.bookstore.demo.dto.OrderResponse;
import com.bookstore.demo.entity.*;
import com.bookstore.demo.exception.ResourceNotFoundException;
import com.bookstore.demo.repository.CartItemRepository;
import com.bookstore.demo.repository.OrderRepository;
import com.bookstore.demo.repository.UserRepository;
import com.bookstore.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse checkout(String email) {

        User user = findUserOrThrow(email);

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Your cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.COMPLETED)
                .totalAmount(0.0)
                .build();

        double total = 0.0;

        for (CartItem cartItem : cartItems) {

            Book book = cartItem.getBook();
            double lineTotal = book.getPrice() * cartItem.getQuantity();
            total += lineTotal;

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .bookId(book.getId())
                    .bookTitle(book.getTitle())
                    .bookAuthor(book.getAuthor())
                    .bookImageUrl(book.getImageUrl())
                    .unitPrice(book.getPrice())
                    .quantity(cartItem.getQuantity())
                    .build();

            order.getItems().add(orderItem);
        }

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByUserId(user.getId());

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getMyOrders(String email) {

        User user = findUserOrThrow(email);

        return orderRepository.findByUserIdOrderByOrderDateDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(String email, Long orderId) {

        User user = findUserOrThrow(email);

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return mapToResponse(order);
    }

    private User findUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> OrderItemResponse.builder()
                        .bookId(item.getBookId())
                        .bookTitle(item.getBookTitle())
                        .bookAuthor(item.getBookAuthor())
                        .bookImageUrl(item.getBookImageUrl())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .lineTotal(item.getUnitPrice() * item.getQuantity())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .items(items)
                .build();
    }
}
