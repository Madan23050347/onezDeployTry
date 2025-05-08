package com.onez.model;

import java.time.LocalDate;
import java.util.List;

public class OrderModel {
    private int orderId;
    private LocalDate orderDate;
    private String orderStatus;
    private List<OrderItemModel> items;  // List of order items
    private UserModel user;
    private String paymentMethod;
    private double totalPrice;
    
    // Constructors
    public OrderModel() {}
    
    public OrderModel(int orderId, LocalDate orderDate, String orderStatus, 
                    List<OrderItemModel> items, UserModel user) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.items = items;
        this.user = user;
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItemModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemModel> items) {
        this.items = items;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	
}