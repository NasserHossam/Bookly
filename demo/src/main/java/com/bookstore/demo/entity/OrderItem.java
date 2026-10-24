package com.bookstore.demo.entity;

import jakarta.persistence.*;
import lombok.*;

// Snapshot fields (bookTitle/bookAuthor/imageUrl/unitPrice) are copied at checkout time
// so a purchase history stays accurate even if the Book is later edited or deleted.
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Nullable: the original book may be deleted later, the snapshot fields below remain.
    @Column(name = "book_id")
    private Long bookId;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private String bookAuthor;

    private String bookImageUrl;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Integer quantity;
}
