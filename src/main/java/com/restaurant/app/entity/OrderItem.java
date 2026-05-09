package com.restaurant.app.entity;

import jakarta.persistence.*;

import lombok.*;

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

    /*
     =====================================
     MENU ITEM
     =====================================
    */

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    /*
     =====================================
     ORDER
     =====================================
    */

    @ManyToOne
    @JoinColumn(name = "order_id")
    private FoodOrder order;

    private Integer quantity;

    private Double price;

    private Double totalPrice;
}