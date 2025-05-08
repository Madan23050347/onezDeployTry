package com.onez.model;

import java.time.LocalDate;
import java.util.List;

public class CartModel {
    private int cartId;
    private UserModel user;
    private int totalItems;
    private double totalPrice;
    private LocalDate createdAt;
    private List<CartItemModel> items;

    public CartModel() {
    }

    public CartModel(int cartId, UserModel user, int totalItems, double totalPrice, 
                   LocalDate createdAt, List<CartItemModel> items) {
        this.cartId = cartId;
        this.user = user;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.items = items;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<CartItemModel> getItems() {
        return items;
    }

    public void setItems(List<CartItemModel> items) {
        this.items = items;
    }
}