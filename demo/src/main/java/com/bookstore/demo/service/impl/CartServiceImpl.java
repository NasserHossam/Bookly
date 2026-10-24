package com.bookstore.demo.service.impl;

import com.bookstore.demo.dto.BookResponse;
import com.bookstore.demo.dto.CartItemRequest;
import com.bookstore.demo.dto.CartItemResponse;
import com.bookstore.demo.dto.UpdateCartItemRequest;
import com.bookstore.demo.entity.Book;
import com.bookstore.demo.entity.CartItem;
import com.bookstore.demo.entity.User;
import com.bookstore.demo.exception.ResourceNotFoundException;
import com.bookstore.demo.repository.BookRepository;
import com.bookstore.demo.repository.CartItemRepository;
import com.bookstore.demo.repository.UserRepository;
import com.bookstore.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public List<CartItemResponse> getCart(String email) {

        User user = findUserOrThrow(email);

        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CartItemResponse addToCart(String email, CartItemRequest request) {

        User user = findUserOrThrow(email);

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        CartItem cartItem = cartItemRepository
                .findByUserIdAndBookId(user.getId(), book.getId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    return existing;
                })
                .orElseGet(() -> CartItem.builder()
                        .user(user)
                        .book(book)
                        .quantity(request.getQuantity())
                        .build());

        CartItem saved = cartItemRepository.save(cartItem);

        return mapToResponse(saved);
    }

    @Override
    public CartItemResponse updateQuantity(String email, Long cartItemId, UpdateCartItemRequest request) {

        User user = findUserOrThrow(email);

        CartItem cartItem = cartItemRepository
                .findByIdAndUserId(cartItemId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItem.setQuantity(request.getQuantity());

        return mapToResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public void removeItem(String email, Long cartItemId) {

        User user = findUserOrThrow(email);

        CartItem cartItem = cartItemRepository
                .findByIdAndUserId(cartItemId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String email) {

        User user = findUserOrThrow(email);

        cartItemRepository.deleteByUserId(user.getId());
    }

    private User findUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private CartItemResponse mapToResponse(CartItem cartItem) {

        Book book = cartItem.getBook();

        BookResponse bookResponse = BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .category(book.getCategory())
                .price(book.getPrice())
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .build();

        double subtotal = book.getPrice() * cartItem.getQuantity();

        return CartItemResponse.builder()
                .id(cartItem.getId())
                .book(bookResponse)
                .quantity(cartItem.getQuantity())
                .subtotal(subtotal)
                .build();
    }
}
