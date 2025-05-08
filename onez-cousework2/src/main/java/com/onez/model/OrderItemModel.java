package com.onez.model;

public class OrderItemModel {
    private int orderItemId;
    private int productId;
    private int quantity;
    private double priceAtOrder;
    private ProductModel product;  // Will be populated when viewing orders
    
    // Constructors
    public OrderItemModel() {}
    
    public OrderItemModel(int productId, int quantity, double priceAtOrder) {
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPriceAtOrder() {
		return priceAtOrder;
	}

	public void setPriceAtOrder(double priceAtOrder) {
		this.priceAtOrder = priceAtOrder;
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}
    
}